package me.mackaroni.testplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DisplayDamage implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            // get the final damage dealt
            double damage = event.getFinalDamage();
            // display the damage in the chat to the attacker (to 2 decimal points)
            attacker.sendMessage("You dealt " + String.format("%.2f", damage) + " damage!");
        }
    }
}
