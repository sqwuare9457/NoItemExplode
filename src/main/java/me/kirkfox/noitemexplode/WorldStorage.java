package me.kirkfox.noitemexplode;

import com.google.gson.Gson;
import org.bukkit.World;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WorldStorage {

    private static List<UUID> worlds = new ArrayList<>();

    public static void addWorld(World w) {
        worlds.add(w.getUID());
        try {
            saveWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeWorld(World w) {
        worlds.remove(w.getUID());
        try {
            saveWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveWorlds() throws IOException {
        Gson gson = new Gson();
        File file = new File(NoItemExplode.getPlugin().getDataFolder().getAbsolutePath() + "/worlds.json");
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        FileWriter writer = new FileWriter(file, false);
        gson.toJson(worlds, writer);
        writer.flush();
        writer.close();
    }

    public static void loadWorlds() throws IOException {
        Gson gson = new Gson();
        File file = new File(NoItemExplode.getPlugin().getDataFolder().getAbsolutePath() + "/worlds.json");
        if (file.exists()) {
            FileReader reader = new FileReader(file);
            UUID[] w = gson.fromJson(reader, UUID[].class);
            worlds = new ArrayList<>(Arrays.asList(w));
        }
    }

    public static int getWorldsProtected() {
        return worlds.size();
    }

    public static boolean isProtectedWorld(World w) {
        return worlds.contains(w.getUID());
    }

}
