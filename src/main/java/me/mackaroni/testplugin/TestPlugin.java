package me.mackaroni.testplugin;

import me.mackaroni.testplugin.Commands.Feed;
import me.mackaroni.testplugin.Commands.Gamble;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // startup console message
        getServer().getConsoleSender().sendMessage("[TEST-PLUGIN]This Plugin Works!");
        // server welcome message / custom server tab list
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        // feed command (working hoverables and 60 sec cd timer)
        Objects.requireNonNull(getCommand("feed")).setExecutor(new Feed());
        // gamble command (uses a lotto spinner/ need more polish)
        Objects.requireNonNull(getCommand("gamble")).setExecutor(new Gamble());
    }

    @Override
    public void onDisable() {

    }
}
