package me.kirkfox.noitemexplode;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
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
            Chunk c = p.getLocation().getChunk();
            boolean isProtectedWorld = WorldStorage.isProtectedWorld(w);
            boolean isProtectedChunk = WorldStorage.isProtectedChunk(c);
            if(args.length == 0) {
                p.sendMessage(ChatColor.BLUE + "This " + ChatColor.BOLD + "world" + ChatColor.RESET + ChatColor.BLUE +
                                " is " + ((isProtectedWorld) ? ChatColor.GREEN + "protected " : ChatColor.RED + "not protected ") +
                                ChatColor.BLUE + "from explosions destroying items!",
                        ChatColor.BLUE + "To change this, use " + ChatColor.GOLD + "/nie " + ((isProtectedWorld) ? "off" : "on"));
                if (isProtectedChunk != isProtectedWorld) {
                    p.sendMessage(ChatColor.BLUE + "This " + ChatColor.BOLD + "chunk" + ChatColor.RESET + ChatColor.BLUE +
                                    " is " + ((isProtectedWorld) ? ChatColor.RED + "not protected " : ChatColor.GREEN + "protected ") +
                                    ChatColor.BLUE + "from explosions destroying items!",
                            ChatColor.BLUE + "To change this, use " + ChatColor.GOLD + "/nie chunk " + ((isProtectedWorld) ? "on" : "off"));
                }
            } else {
                String noPerm = ChatColor.RED + "You don't have permission to toggle %s item explosion protection!";
                String protToggle = "This %1$s is now %2$sprotected from items being destroyed in explosions!";
                boolean chunkAttempt = false;
                switch (args[0]) {
                    case "on":
                        if (!p.hasPermission("noitemexplode.set.world")) {
                            p.sendMessage(String.format(noPerm, "world"));
                        } else if (isProtectedWorld) {
                            p.sendMessage(ChatColor.GREEN + "This world is already protected!");
                        } else {
                            WorldStorage.toggleWorld(w);
                            p.sendMessage(ChatColor.GREEN + String.format(protToggle, "world", ""));
                        }
                        break;
                    case "off":
                        if (!p.hasPermission("noitemexplode.set.world")) {
                            p.sendMessage(String.format(noPerm, "world"));
                        } else if (isProtectedWorld) {
                            WorldStorage.toggleWorld(w);
                            p.sendMessage(ChatColor.RED + String.format(protToggle, "world", "un"));
                        } else {
                            p.sendMessage(ChatColor.RED + "This world is already unprotected!");
                        }
                        break;
                    case "chunk":
                        if (!p.hasPermission("noitemexplode.set.chunk")) {
                            p.sendMessage(String.format(noPerm, "chunk"));
                        } else {
                            if (args.length == 1) {
                                chunkAttempt = true;
                            } else if (args[1].equals("on")) {
                                if (isProtectedChunk) {
                                    p.sendMessage(ChatColor.GREEN + "This chunk is already protected!");
                                } else {
                                    WorldStorage.toggleChunk(c);
                                    p.sendMessage(ChatColor.GREEN + String.format(protToggle, "chunk", ""));
                                }
                            } else if (args[1].equals("off")) {
                                if (isProtectedChunk) {
                                    WorldStorage.toggleChunk(c);
                                    p.sendMessage(ChatColor.RED + String.format(protToggle, "chunk", "un"));
                                } else {
                                    p.sendMessage(ChatColor.RED + "This chunk is already unprotected!");
                                }
                            }
                        }
                        if(!chunkAttempt) break;
                    default:
                        p.sendMessage(ChatColor.RED + "Please specify whether to turn item explosion protection on or off using " +
                                ChatColor.GOLD + "/nie " + ((chunkAttempt) ? "chunk " : "") + "[on|off]");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only online players can use this command!");
        }
        return true;
    }
}
