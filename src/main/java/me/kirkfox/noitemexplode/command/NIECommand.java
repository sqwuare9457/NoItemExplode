package me.kirkfox.noitemexplode.command;

import me.kirkfox.noitemexplode.WorldStorage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NIECommand implements CommandExecutor {

    private static final String NO_PERM = ChatColor.RED + "You don't have permission to toggle %s item explosion protection!";
    private static final String PROT_TOGGLE = "This %1$s is now %2$sprotected from items being destroyed in explosions!";
    private static final String ALREADY = "This %1$s is already %2$sprotected!";

    private Player p;
    private World w;
    private Chunk c;
    private boolean isProtectedWorld;
    private boolean isProtectedChunk;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            p = (Player) sender;
            w = p.getWorld();
            c = p.getLocation().getChunk();
            isProtectedWorld = WorldStorage.isProtectedWorld(w);
            isProtectedChunk = WorldStorage.isProtectedChunk(c);
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
                if (args[0].equals("chunk")) {
                    toggle("chunk", (args.length > 1) ? args[1] : "");
                } else {
                    toggle("world", args[0]);
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only online players can use this command!");
        }
        return true;
    }

    private void toggle(String region, String toggle) {
        boolean isChunk = region.equals("chunk");
        if (toggle.equals("on") || toggle.equals("off")) {
            boolean isOn = toggle.equals("on");
            String prot = (isOn) ? "" : "un";
            ChatColor color = (isOn) ? ChatColor.GREEN : ChatColor.RED;
            if (!p.hasPermission("noitemexplode.set." + region)) {
                p.sendMessage(String.format(NO_PERM, region));
            } else if (((isChunk) ? isProtectedChunk : isProtectedWorld) ^ isOn) {
                if (isChunk) {
                    WorldStorage.toggleChunk(c);
                } else {
                    WorldStorage.toggleWorld(w);
                }
                p.sendMessage(color + String.format(PROT_TOGGLE, region, prot));
            } else {
                p.sendMessage(color + String.format(ALREADY, region, prot));
            }
        } else {
            p.sendMessage(ChatColor.RED + "Please specify whether to turn item explosion protection on or off using " +
                    ChatColor.GOLD + "/nie " + ((isChunk) ? "chunk " : "") + "[on|off]");
        }
    }

}
