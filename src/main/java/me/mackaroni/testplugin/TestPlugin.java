package me.mackaroni.testplugin;

import me.mackaroni.testplugin.Commands.Feed;
import me.mackaroni.testplugin.Commands.Gamble;
import me.mackaroni.testplugin.CustomItems.CustomItem;
import me.mackaroni.testplugin.CustomItems.CustomItemHandler;
import me.mackaroni.testplugin.CustomItems.FlightStick;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.Name;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class TestPlugin extends JavaPlugin {

    public static NamespacedKey rpgItemKey;
    public static Map<String, CustomItem> customItemMap;

    @Override
    public void onEnable() {
        // startup console message
        Bukkit.getConsoleSender().sendMessage("[TEST-PLUGIN]This Plugin Works!");
        // server welcome message / custom server tab list
        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);
        // feed command (working hoverables and 60 sec cd timer)
        Objects.requireNonNull(getCommand("feed")).setExecutor(new Feed());
        // gamble command (uses a lotto spinner/ need more polish)
        Gamble gamble = new Gamble();
        Objects.requireNonNull(getCommand("gamble")).setExecutor(gamble);
        Bukkit.getPluginManager().registerEvents(gamble, this);
        // custom item namespacedkey + initializations
        rpgItemKey = new NamespacedKey(this, "rpg-item-key");
        customItemMap = new HashMap<>();
        registerItems(new FlightStick());
        registerListeners(new CustomItemHandler(), new DisplayDamage());
    }

    // registers each customItem in the hash map
    private void registerItems(CustomItem... customItems) {
        Arrays.asList(customItems).forEach(ci -> customItemMap.put(ci.getId(), ci));
    }

    private void registerListeners(Listener... listeners) {
        Arrays.asList(listeners).forEach(l -> Bukkit.getPluginManager().registerEvents(l, this));
    }

    @Override
    public void onDisable() {
        // Null
    }
}
