package knugel.structurebounds.chunk;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.server.v1_14_R1.StructurePiece;
import org.bukkit.Chunk;

import java.util.Map;
import java.util.Set;

public class StructureCache {
    private static final Cache<Long, Set<StructurePiece>> cache;

    static {
        cache = CacheBuilder
                .newBuilder()
                .maximumSize(128)
                .build();
    }

    private static boolean isCached(Chunk chunk) {
        return cache.getIfPresent(chunk.getChunkKey()) != null;
    }

    private static void put(Chunk chunk, Set<StructurePiece> structurePieces) {
        cache.put(chunk.getChunkKey(), structurePieces);
    }

    public static Set<StructurePiece> get(Chunk chunk) {
        if (!isCached(chunk)) {
            Map<net.minecraft.server.v1_14_R1.Chunk, Set<StructurePiece>> pieces = ChunkHandler.process(chunk);
            for (Map.Entry<net.minecraft.server.v1_14_R1.Chunk, Set<StructurePiece>> entry : pieces.entrySet()) {
                put(entry.getKey().getBukkitChunk(), entry.getValue());
            }
        }
        return cache.getIfPresent(chunk.getChunkKey());
    }
}
