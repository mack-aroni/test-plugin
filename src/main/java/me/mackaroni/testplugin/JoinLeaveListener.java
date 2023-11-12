package me.mackaroni.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        int ping = player.getPing();
        // custom tab header and footer(which shows the players ping to the server)
        final Component header = Component.text("TEST SERVER", NamedTextColor.BLUE);
        final Component footer = Component.text("Ping | "+ping);
        player.sendPlayerListHeaderAndFooter(header, footer);

        // creates a different global join message to all other players
        Component globalMessage = Component.text("[+]"+playerName)
                .color(NamedTextColor.GREEN);
        // replaces default joinMessage with globalMessage
        event.joinMessage(globalMessage);

        String x = "";
        // joinMessage logic for first time / returning player
        if (player.hasPlayedBefore()) {
            x = "back, ";
        }
        else {
            x = "to the server, ";
        }
        // creates a custom join message for the joining player
        Component joinMessage = Component.text("Welcome ")
                .append(Component.text(x))
                .color(NamedTextColor.GREEN)
                .append(Component.text(playerName)
                        .color(NamedTextColor.GOLD))
                .append(Component.text("!")
                        .color(NamedTextColor.GREEN));
        // sends joinMessage to joining player
        event.getPlayer().sendMessage(joinMessage);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        // creates a leave message for all players
        Component leaveMessage = Component.text("[-]"+playerName)
                .color(NamedTextColor.RED);
        // sends leaveMessage to all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(leaveMessage);
        }
    }

}
