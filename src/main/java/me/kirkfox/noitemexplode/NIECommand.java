package me.kirkfox.noitemexplode;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NIECommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            World w = p.getWorld();
            boolean isProtected = WorldStorage.isProtectedWorld(p.getWorld());
            if(args.length == 0) {
                p.sendMessage(ChatColor.BLUE + "This world is " +
                        ((isProtected) ? ChatColor.GREEN + "protected " : ChatColor.RED + "not protected ") +
                        ChatColor.BLUE + "from explosions destroying items!",
                        ChatColor.BLUE + "To change this, use " + ChatColor.GOLD + "/nie " + ((isProtected) ? "off" : "on"));
            } else {
                switch (args[0]) {
                    case "on":
                        if(isProtected) {
                            p.sendMessage(ChatColor.GREEN + "This world is already protected!");
                        } else {
                            WorldStorage.addWorld(w);
                            p.sendMessage(ChatColor.GREEN + "This world is now protected from items being destroyed in explosions!");
                        }
                        break;
                    case "off":
                        if(isProtected) {
                            WorldStorage.removeWorld(w);
                            p.sendMessage(ChatColor.RED + "This world is no longer protected from items being destroyed in explosions!");
                        } else {
                            p.sendMessage(ChatColor.RED + "This world is already unprotected!");
                        }
                        break;
                    default:
                        p.sendMessage(ChatColor.RED + "Please specify whether to turn item explosion protection on or off using " +
                                ChatColor.GOLD + "/nie [on|off]");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only online players can use this command!");
        }
        return true;
    }
}
