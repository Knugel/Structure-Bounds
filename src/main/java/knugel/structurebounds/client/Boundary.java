package knugel.structurebounds.client;

import knugel.structurebounds.Config;
import knugel.structurebounds.chunk.StructureCache;
import net.minecraft.server.v1_14_R1.StructureBoundingBox;
import net.minecraft.server.v1_14_R1.StructurePiece;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Boundary {
    private final UUID uuid;
    private final Config config;

    private Player player;
    private boolean isVisible;

    public Boundary(UUID uuid, Config config) {
        this.uuid = uuid;
        this.config = config;

        this.player = Bukkit.getPlayer(uuid);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void toggle() {
        this.isVisible = !isVisible;
    }

    public void render() {
        if (!isVisible || !player.isOnline()) return;

        Location current = player.getLocation();

        Chunk chunk = current.getChunk();
        World world = chunk.getWorld();

        Set<Chunk> chunks = getNeighbors(world, chunk);

        int radius = config.getRadiusSquared();
        Particle.DustOptions options = new Particle.DustOptions(config.getColor(), 1);

        Set<StructurePiece> pieces = chunks
                .stream()
                .map(StructureCache::get)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        for (StructurePiece piece : pieces) {
            StructureBoundingBox box = piece.g();
            Set<Location> outline = getOutline(box, world, config.getParticleDistance());

            for(Location location : outline) {
                if (location.distanceSquared(current) > radius) {
                    continue;
                }

                Block block = world.getBlockAt(location);
                if (!block.isPassable()) continue;

                player.spawnParticle(
                        Particle.REDSTONE,
                        new Location(world, location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5),
                        1,
                        options);
            }
        }
    }

    private Set<Location> getOutline(StructureBoundingBox box, World world, float distance) {
        Set<Location> ret = new HashSet<>();

        float minX = Math.min(box.a, box.d);
        float minY = Math.min(box.b, box.e);
        float minZ = Math.min(box.c, box.f);
        float maxX = Math.max(box.a, box.d);
        float maxY = Math.max(box.b, box.e);
        float maxZ = Math.max(box.c, box.f);

        for (double x = minX; x <= maxX; x+=distance)
            for (double y = minY; y <= maxY; y+=distance)
                for (double z = minZ; z <= maxZ; z+=distance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        ret.add(new Location(world, x, y, z));
                    }
                }
        return ret;
    }

    private Set<Chunk> getNeighbors(World world, Chunk center) {
        Set<Chunk> ret = new HashSet<>();
        for(int x = center.getX() - 1; x <= (center.getX() + 1); x++)
            for(int z = center.getZ() - 1; z <= (center.getZ() + 1); z++)
                ret.add(world.getChunkAt(x, z));
        return ret;
    }
}
