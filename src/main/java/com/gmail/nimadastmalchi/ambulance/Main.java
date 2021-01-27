package com.gmail.nimadastmalchi.ambulance;

import com.gmail.nimadastmalchi.ambulance.commands.AmbulanceCommand;
import com.gmail.nimadastmalchi.ambulance.listeners.EntityDamageListener;
import com.gmail.nimadastmalchi.ambulance.listeners.PlayerMoveListener;
import com.gmail.nimadastmalchi.ambulance.listeners.VehicleMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends JavaPlugin {
    public static final float DIV_FACT = (float) 10;
    public static final float DISTANCE_THRESHOLD = 4;
    public static HashMap<String, Minecart> players;
    public static ReentrantLock lock;

    @Override
    public void onEnable() {
        players = new HashMap<>();
        lock = new ReentrantLock();

        new AmbulanceCommand(this);
        new EntityDamageListener(this);
        new PlayerMoveListener(this);
        //new VehicleMoveListener(this);

        /*
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Main.lock.lock();
                    try {
                        for (Map.Entry entry : players.entrySet()) {
                            Minecart m = (Minecart) entry.getValue();
                            if (m == null) {
                                continue;
                            }
                            String n = (String) entry.getKey();
                            Player p = Bukkit.getPlayer(n);
                            Location pLoc = p.getLocation();
                            Location mLoc = m.getLocation();
                            Vector v = normalizeVector(new Vector(pLoc.getX() - mLoc.getX(), pLoc.getY() - mLoc.getY(), pLoc.getZ() - mLoc.getZ()), Main.MAX_SPEED);
                            m.setDerailedVelocityMod(v);
                            m.setVelocity(v);
                            if (pLoc.distanceSquared(mLoc) < DISTANCE_THRESHOLD) {
                                ThrownPotion potion = (ThrownPotion) p.getWorld().spawnEntity(mLoc, EntityType.SPLASH_POTION);
                                Main.players.put(n, null);
                                break;
                            }
                        }
                    } finally {
                        Main.lock.unlock();
                    }
                }
            }
        });
        th.start();
         */
    }

    public static Vector normalizeVector(Vector v, float divFact) {
        return new Vector(v.getX() / divFact, v.getY() / divFact, v.getZ() / divFact);
    }
}
