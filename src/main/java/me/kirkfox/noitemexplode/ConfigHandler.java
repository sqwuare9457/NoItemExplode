package me.kirkfox.noitemexplode;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigHandler {

    private static boolean whitelist = false;

    private static final Set<Material> ITEM_FILTER = new HashSet<>();
    private static final Set<EntityType> PROTECTED_ENTITIES = new HashSet<>();

    private static final String[][] ENTITIES = {{"creeper"}, {"fireball", "small_fireball"}, {"tnt", "primed_tnt"},
            {"wither", "wither_skull"}};

    public static void loadConfig(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        whitelist = config.getBoolean("use-filter-as-whitelist");
        List<String> filter = config.getStringList("filter");
        for(String i : filter) {
            Material m = Material.getMaterial(i.toUpperCase());
            if (m != null) {
                ITEM_FILTER.add(m);
            }
        }
        ConfigurationSection pe = config.getConfigurationSection("protected-explosions");
        for(String[] e : ENTITIES) {
            if (pe == null || pe.getBoolean(e[0])) {
                for(String et : e) {
                    if(!et.equals("tnt")) PROTECTED_ENTITIES.add(EntityType.valueOf(et.toUpperCase()));
                }
            }
        }
    }

    public static boolean shouldProtect(Material m, EntityType e) {
        return PROTECTED_ENTITIES.contains(e) && (ITEM_FILTER.contains(m) == whitelist);
    }

}
