package com.gmail.nimadastmalchi.ambulance.listeners;

import com.gmail.nimadastmalchi.ambulance.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class VehicleMoveListener implements Listener {
    private Main plugin;

    public VehicleMoveListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(VehicleMoveEvent e) {
        Vehicle v = e.getVehicle();
        if (v instanceof Minecart) {
            Minecart m = (Minecart) v;
            String n = "";
            // Find the Player that corresponds to the Minecart that moved:
            for (Map.Entry entry : Main.players.entrySet()) {
                Minecart currentM = (Minecart) entry.getValue();
                String currentN = (String) entry.getKey();
                if (m == currentM) {
                    n = currentN;
                }
            }
            // Check if there is a Player that corresponds to the Minecart that moved:
            if (n == "") {
                return;
            }
            Player p = Bukkit.getPlayer(n);
            Location pLoc = p.getLocation();
            Location mLoc = m.getLocation();

            // Check if the movement is caused by player or not (if distance to player has increased, "cancel" event):
            if (pLoc.distanceSquared(e.getTo()) > pLoc.distanceSquared(e.getFrom())) {
                m.teleport(e.getFrom());
            }

            Vector vel = Main.normalizeVector(new Vector(pLoc.getX() - mLoc.getX(), pLoc.getY() - mLoc.getY(), pLoc.getZ() - mLoc.getZ()), Main.DIV_FACT);
            p.sendMessage(vel.toString());
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
