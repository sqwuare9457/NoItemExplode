package me.kirkfox.noitemexplode;

import me.kirkfox.noitemexplode.listener.EntityDamageByEntityListener;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class NoItemExplode extends JavaPlugin {

    private static JavaPlugin plugin;

    private static final int BSTATS_ID = 12928;

    @Override
    public void onEnable() {
        plugin = this;

        ConfigHandler.loadConfig(plugin);

        Objects.requireNonNull(getCommand("noitemexplode")).setExecutor(new NIECommand());
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);

        try {
            WorldStorage.loadWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Metrics metrics = new Metrics(this, BSTATS_ID);
        metrics.addCustomChart(new SimplePie("worldsProtected", WorldStorage::getWorldsProtected));

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
