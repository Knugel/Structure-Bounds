package knugel.structurebounds.client;

import knugel.structurebounds.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoundaryManager {
    private final Map<UUID, Boundary> boundaryMap;
    private final Config config;

    public BoundaryManager(JavaPlugin plugin, Config config) {
        this.boundaryMap = new HashMap<>();
        this.config = config;

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Boundary boundary : boundaryMap.values()) {
                    boundary.render();
                }
            }
        }.runTaskTimer(plugin, config.getFrequency() + 20, config.getFrequency());
    }

    public void add(UUID uuid) {
        Boundary boundary = new Boundary(uuid, config);
        boundaryMap.put(uuid, boundary);
    }

    public Boundary get(UUID uuid) {
        return boundaryMap.get(uuid);
    }

    public void remove(UUID uuid) {
        boundaryMap.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return boundaryMap.containsKey(uuid);
    }
}
