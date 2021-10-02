package me.kirkfox.noitemexplode;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler {

    private static final Set<EntityType> PROTECTED_ENTITIES = new HashSet<>();

    private static final String[] NAMES = {"creeper", "fireball", "tnt", "wither"};
    private static final EntityType[][] ENTITIES = {{EntityType.CREEPER}, {EntityType.FIREBALL, EntityType.SMALL_FIREBALL},
            {EntityType.PRIMED_TNT}, {EntityType.WITHER, EntityType.WITHER_SKULL}};

    public static void loadConfig(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        for(int i = 0; i < NAMES.length; i++) {
            if(config.getBoolean("protect-from-" + NAMES[i] + "-explosions")) {
                PROTECTED_ENTITIES.addAll(Arrays.asList(ENTITIES[i]));
            }
        }
    }

    public static boolean isProtectedEntity(EntityType e) {
        return PROTECTED_ENTITIES.contains(e);
    }

}
