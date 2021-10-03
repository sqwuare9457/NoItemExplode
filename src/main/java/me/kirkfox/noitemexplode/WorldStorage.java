package me.kirkfox.noitemexplode;

import com.google.gson.Gson;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldStorage {

    private static Set<World> worlds = new HashSet<>();
    private static Set<Chunk> chunks = new HashSet<>();

    public static void toggleWorld(World w) {
        if ((worlds.contains(w))) {
            worlds.remove(w);
        } else {
            worlds.add(w);
        }
        chunks.removeIf(c -> c.getWorld() == w);
        try {
            saveWorlds();
            saveChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toggleChunk(Chunk c) {
        if(chunks.contains(c)) {
            chunks.remove(c);
        } else {
            chunks.add(c);
        }

        try {
            saveChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveWorlds() throws IOException {
        Gson gson = new Gson();
        File file = new File(NoItemExplode.getPlugin().getDataFolder().getAbsolutePath() + "/worlds.json");
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        FileWriter writer = new FileWriter(file, false);
        List<String> worldData = new ArrayList<>();
        for(World w : worlds) {
            worldData.add(w.getName());
        }
        gson.toJson(worldData, writer);
        writer.flush();
        writer.close();
    }

    public static void loadWorlds() throws IOException {
        Gson gson = new Gson();
        File file = new File(NoItemExplode.getPlugin().getDataFolder().getAbsolutePath() + "/worlds.json");
        if (file.exists()) {
            FileReader reader = new FileReader(file);
            String[] worldData = gson.fromJson(reader, String[].class);
            Set<World> worldSet = new HashSet<>();
            for(String w : worldData) {
                worldSet.add(NoItemExplode.getPluginServer().getWorld(w));
            }
            worlds = worldSet;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveChunks() throws IOException {
        Gson gson = new Gson();
        File file = new File(NoItemExplode.getPlugin().getDataFolder().getAbsolutePath() + "/chunks.json");
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        FileWriter writer = new FileWriter(file, false);
        List<String> chunkData = new ArrayList<>();
        for(Chunk c : chunks) {
            chunkData.add(c.getWorld().getName() + ":" + c.getX() + ":" + c.getZ());
        }
        gson.toJson(chunkData, writer);
        writer.flush();
        writer.close();
    }

    public static void loadChunks() throws IOException {
        Gson gson = new Gson();
        File file = new File(NoItemExplode.getPlugin().getDataFolder().getAbsolutePath() + "/chunks.json");
        if (file.exists()) {
            FileReader reader = new FileReader(file);
            String[] chunkData = gson.fromJson(reader, String[].class);
            Set<Chunk> chunkSet = new HashSet<>();
            for(String c : chunkData) {
                String[] data = c.split(":");
                chunkSet.add(NoItemExplode.getPluginServer().getWorld(data[0])
                        .getChunkAt(Integer.parseInt(data[1]), Integer.parseInt(data[2])));
            }
            chunks = chunkSet;
        }
    }

    public static String getWorldsProtected() {
        int w = worlds.size();
        if (w < 6) {
            return String.valueOf(w);
        }
        if (w < 11) {
            return "6-10";
        }
        if (w < 21) {
            return "11-20";
        }
        return ">20";
    }

    public static boolean isProtectedWorld(World w) {
        return worlds.contains(w);
    }

    public static boolean isProtectedChunk(Chunk c) {
        return chunks.contains(c) ^ worlds.contains(c.getWorld());
    }

}
