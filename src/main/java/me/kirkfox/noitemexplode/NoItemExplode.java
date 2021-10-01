package me.kirkfox.noitemexplode;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class NoItemExplode extends JavaPlugin {

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Objects.requireNonNull(getCommand("noitemexplode")).setExecutor(new NIECommand());
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        try {
            WorldStorage.loadWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        try {
            WorldStorage.saveWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
