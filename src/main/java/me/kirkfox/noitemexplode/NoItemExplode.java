package me.kirkfox.noitemexplode;

import me.kirkfox.noitemexplode.command.NIECommand;
import me.kirkfox.noitemexplode.command.NIETabCompleter;
import me.kirkfox.noitemexplode.listener.EntityDamageByEntityListener;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public final class NoItemExplode extends JavaPlugin {

    private static JavaPlugin plugin;

    private static final int BSTATS_ID = 12928;

    @Override
    public void onEnable() {
        plugin = this;

        ConfigHandler.loadConfig(plugin);

        PluginCommand cmd = Objects.requireNonNull(getCommand("noitemexplode"));
        cmd.setExecutor(new NIECommand());
        cmd.setTabCompleter(new NIETabCompleter());

        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);

        try {
            RegionStorage.loadWorlds();
            RegionStorage.loadChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Metrics metrics = new Metrics(this, BSTATS_ID);
        metrics.addCustomChart(new SimplePie("worldsProtected", RegionStorage::getWorldsProtected));

        checkForUpdates();

    }

    @Override
    public void onDisable() {
        try {
            RegionStorage.saveWorlds();
            RegionStorage.saveChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try(InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=96571").openStream();
                Scanner scanner = new Scanner(inputStream)) {
                if(scanner.hasNext()) {
                    String version = scanner.next();
                    if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                        this.getLogger().info("A new version of NoItemExplode is available. " +
                                "Go to https://www.spigotmc.org/resources/noitemexplode.96571/ for NoItemExplode v" + version);
                    }
                }
            } catch (IOException e) {
                this.getLogger().info("Cannot look for updates: " + e.getMessage());
            }
        });
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static Server getPluginServer() {
        return plugin.getServer();
    }
}
