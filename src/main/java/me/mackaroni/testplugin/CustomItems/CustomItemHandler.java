package me.mackaroni.testplugin.CustomItems;

import me.mackaroni.testplugin.TestPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemHandler implements Listener {

    // gives flightstick on player join (instead of using a command)
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().addItem(TestPlugin.customItemMap.get("FlightStick").getItem());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        // checks if it's a custom item
        if (isCustomItem(heldItem)) {
            // handles actions on lclick
            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                CustomItem customItem = TestPlugin.customItemMap.get(getItemId(heldItem));
                customItem.handleLeftClick(player, heldItem, event);
            }

            // handles actions on rclick
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                CustomItem customItem = TestPlugin.customItemMap.get(getItemId(heldItem));
                customItem.handleRightClick(player, heldItem, event);
            }
        }

    }

    private boolean isCustomItem(ItemStack itemStack) {
        return (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(TestPlugin.rpgItemKey, PersistentDataType.STRING));
    }

    private String getItemId(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(TestPlugin.rpgItemKey, PersistentDataType.STRING);
    }

}