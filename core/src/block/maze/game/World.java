package block.maze.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 14/04/2017.
 */
public class World implements IBlockAccess {
    Map chunks = new HashMap<String,Chunk>();

    @Override
    public int getBlock(int x, int y, int z) {
        Chunk chunk = getChunk(x>>4,y>>4,z>>4);
        if(chunk==null) return 0;
        int block = chunk.getBlock(x%16,y%16,z%16);
        return block;
    }

    @Override
    public int setBlock(int block, int x, int y, int z) {
        Chunk chunk = getChunk(x>>4,y>>4,z>>4);
        if(chunk==null) {
            chunk = new Chunk();
            chunk.initChunk(x>>4,y>>4,z>>4);
            chunks.put(chunkKey(x>>4,y>>4,z>>4),chunk);
        }
        chunk.setBlock(block,x%16,y%16,z%16 );
        return block;
    }

    //Get chunk (in chunk coords)
    private Chunk getChunk(int x, int y, int z) {
        Chunk chunk = (Chunk)chunks.get(chunkKey(x,y,z));
        return chunk;
    }

    private String chunkKey(int x, int y, int z) {
        return String.format("%d:%d:%d",x,y,z);
    }
}
