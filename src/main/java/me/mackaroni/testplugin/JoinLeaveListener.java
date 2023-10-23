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
        Player sender = event.getPlayer();
        String playerName = sender.getName();
        int ping = sender.getPing();
        // custom header and footer
        final Component header = Component.text("My Cool Server", NamedTextColor.BLUE);
        final Component footer = Component.text("Ping | "+ping);
        sender.sendPlayerListHeaderAndFooter(header, footer);
        // creates a different global join message to all other players
        Component globalMessage = Component.text("+"+playerName)
                .color(NamedTextColor.GREEN);
        // sends globalMessage to all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            // only send globalMessage to all -other- players
            // if statement blocked out for debug reasons
            //if (!player.getUniqueId().equals(sender.getUniqueId())) {
                player.sendMessage(globalMessage);
            //}
        }
        // creates a custom join message for the joining player
        Component joinMessage = Component.text("Welcome to the server, ")
                .color(NamedTextColor.GREEN)
                .append(Component.text(playerName)
                        .color(NamedTextColor.GOLD))
                .append(Component.text("!")
                        .color(NamedTextColor.GREEN));
        // sends joinMessage to joining player
        event.getPlayer().sendMessage(joinMessage);
    }

    public void setCustomTabList(Player player, String customPlayerName, int ping) {
        Component tabList = Component.text()
                .append(Component.text("Custom Tab List Header", NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.text(customPlayerName, NamedTextColor.AQUA))
                .append(Component.text(" | Ping: " + ping, NamedTextColor.GRAY))
                .build();
        player.playerListName(tabList);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        // creates a leave message for all players
        Component leaveMessage = Component.text("-"+playerName)
                .color(NamedTextColor.RED);
        // sends leaveMessage to all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(leaveMessage);
        }
    }

}
