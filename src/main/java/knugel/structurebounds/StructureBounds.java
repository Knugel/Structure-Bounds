package knugel.structurebounds;

import knugel.structurebounds.chunk.ChunkHandler;
import knugel.structurebounds.client.BoundaryManager;
import knugel.structurebounds.client.PlayerHandler;
import knugel.structurebounds.command.BoundaryCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class StructureBounds extends JavaPlugin {

    @Override
    public void onEnable() {
        Config config = readConfig();
        BoundaryManager manager = new BoundaryManager(this, config);

        getServer().getPluginManager().registerEvents(new ChunkHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(manager), this);

        getCommand("sbb").setExecutor(new BoundaryCommand(manager));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    private Config readConfig() {
        saveDefaultConfig();

        int radius = getConfig().getInt("radius");
        float distance = (float)getConfig().getDouble("distance");
        int color = getConfig().getInt("color");
        int frequency = getConfig().getInt("frequency");
        return new Config(radius, distance, color, frequency);
    }
}
