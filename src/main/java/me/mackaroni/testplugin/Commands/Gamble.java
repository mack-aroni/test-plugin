package me.mackaroni.testplugin.Commands;

import me.mackaroni.testplugin.TestPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Gamble implements CommandExecutor, Listener {

    List<Inventory> invs = new ArrayList<>();
    public static ItemStack[] contents;
    private int itemIndex = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // checks if sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        // incorrect usage
        if (args.length > 0) {
            // incorrect usage
            player.sendMessage(Component.text("Usage: /gamble").color(NamedTextColor.RED));
            return true;
        }
        // defines what item is the gamble "token"
        Material token = Material.DIAMOND;
        ItemStack tokenFee = new ItemStack(token);
        // amount of token needed
        int amount = 3;
        tokenFee.setAmount(amount);
        // check if playerHand has token in proper amount or greater
        if (player.getInventory().getItemInMainHand().isSimilar(tokenFee)) {
            // remove token cost and spin
            player.getInventory().removeItem(tokenFee);
            spin(player);
        }
        // doesn't have proper amount of token/s
        else {
            player.sendMessage(Component.text("You need "+amount+" "+token+" to gamble!").color(NamedTextColor.RED));
        }
        return true;
    }

    // setup for shuffle GUI
    public void shuffle(Inventory inv) {
        // actual gamble rewards (equal probabilities)
        if (contents == null) {
            ItemStack[] items = new ItemStack[5];
            items[0] = new ItemStack(Material.DIAMOND,1);
            items[1] = new ItemStack(Material.DIAMOND,2);
            items[2] = new ItemStack(Material.DIAMOND,3);
            items[3] = new ItemStack(Material.DIAMOND,4);
            items[4] = new ItemStack(Material.NETHER_STAR,1);
            contents = items;
        }
        // spinner GUI
        int startingIndex = ThreadLocalRandom.current().nextInt(contents.length);
        for (int i = 0; i < startingIndex; i++) {
            // set the spinner in the center of the 3 rows (indices 9-17)
            for (int itemStacks = 9; itemStacks < 18; itemStacks++) {
                inv.setItem(itemStacks, contents[(itemStacks + itemIndex) % contents.length]);
            }
            itemIndex ++;
        }
        // cosmetic center "bars"
        ItemStack item = new ItemStack(Material.END_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(""));
        item.setItemMeta(meta);
        inv.setItem(4, item);
        inv.setItem(22, item);
    }

    // actual gamble spinner call
    public void spin(final Player player) {
        // creates 3 row inventory GUI
        Inventory inv = Bukkit.createInventory(null, 27,
                Component.text("Good Luck")
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD));
        // shuffles/setup for GUI and adds to invs + opens spinner screen
        shuffle(inv);
        invs.add(inv);
        player.openInventory(inv);
        // spins between 7 and 12 seconds
        Random r = new Random();
        double seconds = 7.0 + (12.0 - 7.0) * r.nextDouble();
        new BukkitRunnable() {
            double delay = 0;
            int ticks = 0;
            boolean done = false;
            public void run() {
                if (done) {
                    return;
                }
                ticks++;
                delay += 1 / (20 * seconds);
                // actual GUI movement
                if (ticks > delay * 10) {
                    ticks = 0;
                    for (int itemStacks = 9; itemStacks < 18; itemStacks++) {
                        inv.setItem(itemStacks, contents[(itemStacks + itemIndex) % contents.length]);
                    }
                    itemIndex++;
                    if (delay >= 0.5) {
                        done = true;
                        new BukkitRunnable() {
                            public void run() {
                                // gives reward to player
                                ItemStack item = inv.getItem(13);
                                assert item != null;
                                player.getInventory().addItem(item);
                                // reward message
                                if (item.getAmount() == 1) {
                                    player.sendMessage(Component.text("You won a "+item.getType()+"!"));
                                }
                                else {
                                    player.sendMessage(Component.text("You won "+item.getAmount()+" "+item.getType()+"s!"));
                                }
                                //player.updateInventory();
                                player.closeInventory();
                                cancel();
                            }
                        }.runTaskLater(TestPlugin.getPlugin(TestPlugin.class), 50);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(TestPlugin.getPlugin(TestPlugin.class), 0, 1);
    }

    // prevents items being taken from spinner GUI
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // only triggers for inventories in invs
        if (!invs.contains(event.getInventory())) {
            return;
        }
        event.setCancelled(true);
    }

}
