package me.mackaroni.testplugin.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Feed implements CommandExecutor {
    private final Map<UUID, Boolean> clickConfirmation = new HashMap<>();
    private final Map<UUID, Long> lastCommandTime = new HashMap<>();
    private final int cooldownTimeInSeconds = 60;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // checks if the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        // checks if the player has the permission to use the command
        if (!player.hasPermission("TestPlugin.feed")) {
            player.sendMessage("You do not have permission to use this command");
            return true;
        }
        // obtains player UUID, remaining cooldown, and last usage timestamp
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis() / 1000;
        long lastCommandTimestamp = lastCommandTime.getOrDefault(playerUUID, 0L);
        // correct usage case
        if (args.length == 0) {
            // debug
            //Bukkit.getConsoleSender().sendMessage(currentTime + " " + lastCommandTimestamp + " " + (currentTime - lastCommandTimestamp) + " " + cooldownTimeInSeconds);
            // checks player cooldown, if true returns a message stating the remaining time
            if (currentTime - lastCommandTimestamp < cooldownTimeInSeconds) {
                long remainingTime = cooldownTimeInSeconds - (currentTime - lastCommandTimestamp);
                player.sendMessage(Component.text("You must wait " + remainingTime + " seconds"));
                return true;
            }
            // creates message with clickable options
            Component confirmMessage = Component.text("Confirm")
                    .color(NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to confirm")))
                    .clickEvent(ClickEvent.runCommand("/feed confirm"));
            Component denyMessage = Component.text("Deny")
                    .color(NamedTextColor.RED)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to deny")))
                    .clickEvent(ClickEvent.runCommand("/feed deny"));
            Component message = Component.text("Do you want to be fed? ")                     .decorate(TextDecoration.BOLD)
                    .append(confirmMessage)
                    .append(Component.text(" "))
                    .append(denyMessage);
            // sends message to the player
            player.sendMessage(message);
            // register that the player has seen the event
            clickConfirmation.put(playerUUID, false);
            return true;
        }
        // helper style calls
        if (args[0].equalsIgnoreCase("confirm")) {
            // checks if the player has already accessed the event
            if (clickConfirmation.getOrDefault(playerUUID, false)) {
                return true;
            }
            // sets the player's food level to full
            player.setFoodLevel(20);
            player.sendMessage(Component.text("You have been fed").color(NamedTextColor.GREEN));
            // register that the player has responded to the event/update the time of use
            clickConfirmation.put(playerUUID, true);
            lastCommandTime.put(playerUUID, currentTime);
            return true;
        }
        else if (args[0].equalsIgnoreCase("deny")) {
            // checks if the player has already accessed the event
            if (clickConfirmation.getOrDefault(playerUUID, false)) {
                return true;
            }
            player.sendMessage(Component.text("You have not been fed").color(NamedTextColor.RED));
            // register that the player has responded to the event
            clickConfirmation.put(playerUUID, true);
            return true;
        }
        // incorrect usage
        player.sendMessage(Component.text("Usage: /feed").color(NamedTextColor.RED));
        return true;
    }

}
