package com.gmail.nimadastmalchi.ambulance.listeners;

import com.gmail.nimadastmalchi.ambulance.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class PlayerMoveListener implements Listener {
    private Main plugin;

    public PlayerMoveListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        String n = p.getName();
        if (Main.players.containsKey(n)) {
            Minecart m = Main.players.get(n);
            // Make sure the minecart is not null:
            if (m == null) {
                return;
            }
            Location pLoc = p.getLocation();
            Location mLoc = m.getLocation();
            Vector vel = Main.normalizeVector(new Vector(pLoc.getX() - mLoc.getX(), pLoc.getY() - mLoc.getY(), pLoc.getZ() - mLoc.getZ()), Main.DIV_FACT);
            // Check if the Minecart is close enough:
            if (pLoc.distanceSquared(mLoc) < Main.DISTANCE_THRESHOLD) {
                ThrownPotion potion = (ThrownPotion) p.getWorld().spawnEntity(mLoc, EntityType.SPLASH_POTION);
                potion.setVelocity(vel);
                Main.players.put(n, null);
                // Since the Minecart is not close enough, change the velocity:
            } else {
                m.setDerailedVelocityMod(vel);
                m.setVelocity(vel);
            }
        }
    }
}
