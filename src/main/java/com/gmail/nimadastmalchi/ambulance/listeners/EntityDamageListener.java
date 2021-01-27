package com.gmail.nimadastmalchi.ambulance.listeners;

import com.gmail.nimadastmalchi.ambulance.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class EntityDamageListener implements Listener {
    private Main plugin;
    private final float OFFSET = 10;
    private final double THRESHOLD_HEALTH = 18;

    public EntityDamageListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity en = e.getEntity();
        if (en instanceof Player) {
            Player p = (Player) en;
            String n = p.getName();
            p.sendMessage("Before if: " + p.getHealth());
            double newHealth = p.getHealth() - e.getDamage();
            Main.lock.lock();
            try {
                // Continue if the player is contained in Main.players, if the player does not have an existing minecart, and if the player's health is sufficiently low to spawn a minecart.
                if (Main.players.containsKey(n) && Main.players.get(n) == null && newHealth <= THRESHOLD_HEALTH && newHealth != 0) {
                    p.sendMessage("After");
                    Location pLoc = p.getLocation();
                    Random rand = new Random();
                    // Minecart's location is randomly generated within 10 blocks of each component of the player's location:
                    Location mLoc = new Location(p.getWorld(), pLoc.getX() + OFFSET + rand.nextFloat() * OFFSET, pLoc.getY(), pLoc.getZ() + OFFSET + rand.nextFloat() * OFFSET);
                    // Make sure the minecart is not spawned inside a wall:
                    while (!mLoc.clone().getBlock().isPassable() || !mLoc.clone().add(0, 1, 0).getBlock().isPassable()) {
                        mLoc.add(0, 1, 0);
                    }
                    World w = p.getWorld();
                    Minecart m = w.spawn(mLoc, RideableMinecart.class);
                    Main.players.put(n, m);
                    m.addPassenger(w.spawnEntity(mLoc, EntityType.VILLAGER));
                    Vector vel = Main.normalizeVector(new Vector(pLoc.getX() - mLoc.getX(), pLoc.getY() - mLoc.getY(), pLoc.getZ() - mLoc.getZ()), Main.DIV_FACT);
                    m.setDerailedVelocityMod(vel);
                    m.setVelocity(vel);
                }
            } finally {
                Main.lock.unlock();
            }
        }
    }
}
