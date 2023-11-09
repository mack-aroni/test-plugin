package me.mackaroni.testplugin.CustomItems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class FlightStick extends CustomItem {
    @Override
    public String getName() {
        return "Flight Stick";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("Right Click: Launch yourself through the air");
    }

    @Override
    public void handleLeftClick(Player player, ItemStack itemStack, PlayerInteractEvent event) {
        return;
    }

    @Override
    public void handleRightClick(Player player, ItemStack itemStack, PlayerInteractEvent event) {
        Location location = player.getLocation();
        Vector direction = location.getDirection();

        player.setVelocity(direction.setY(0.5).multiply(2.5));
        player.playSound(player, Sound.BLOCK_LAVA_EXTINGUISH, 0.5F,1F);
    }
}