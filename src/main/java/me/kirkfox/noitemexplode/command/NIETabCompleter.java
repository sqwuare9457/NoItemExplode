package me.kirkfox.noitemexplode.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NIETabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = new ArrayList<>(Arrays.asList("on", "off", "chunk"));
        if (args.length == 1) {
            return options;
        }
        options.remove("chunk");
        if (args.length == 2 && args[0].equals("chunk")) {
            return options;
        }
        return null;
    }
}
