package com.gmail.nimadastmalchi.ambulance.commands;

import com.gmail.nimadastmalchi.ambulance.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmbulanceCommand implements CommandExecutor {
    private Main plugin;

    public AmbulanceCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("ambulance").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }
        Player p = (Player) sender;
        if (!p.isOp()) {
            p.sendMessage(ChatColor.RED + "You do not have sufficient permissions to execute this command.");
            return true;
        }

        String n = p.getName();
        Main.lock.lock();
        try {
            if (Main.players.containsKey(n)) {
                Main.players.remove(n);
                p.sendMessage("Ambulance: " + ChatColor.RED + "[ON] " + ChatColor.GREEN + "[Off]");
            } else {
                Main.players.put(n, null);
                p.sendMessage("Ambulance: " + ChatColor.GREEN + "[ON] " + ChatColor.RED + "[Off]");
            }
        } finally {
            Main.lock.unlock();
        }
        return true;
    }
}
