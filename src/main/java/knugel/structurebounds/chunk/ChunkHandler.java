package knugel.structurebounds.chunk;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.craftbukkit.v1_14_R1.CraftChunk;
import org.bukkit.event.Listener;

import java.util.*;

public class ChunkHandler implements Listener {
    static Map<Chunk, Set<StructurePiece>> process(org.bukkit.Chunk chunk) {
        CraftChunk craft = (CraftChunk)chunk;
        Chunk handle = craft.getHandle();
        World world = handle.getWorld();

        Map<Chunk, Set<StructurePiece>> structurePieces = new HashMap<>();

        for(Map.Entry<String, StructureStart> entry : handle.h().entrySet()) {
            String type = entry.getKey();

            StructureStart start = entry.getValue();
            if(start == null) continue;

            List<StructurePiece> pieces = start.d();

            for(StructurePiece piece : pieces) {
                StructureBoundingBox box = piece.g();

                Chunk current = world.getChunkAtWorldCoords(new BlockPosition(box.a, box.b, box.c));
                structurePieces.compute(current, (c, s) -> {
                   if(s == null)
                       s = new HashSet<>();
                   s.add(piece);
                   return s;
                });
            }
        }
        return structurePieces;
    }
}
