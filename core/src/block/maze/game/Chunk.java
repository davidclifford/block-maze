package block.maze.game;

/**
 * Created by David on 14/04/2017.
 */
public class Chunk {
    static final int HEIGHT = 16;
    static final int WIDTH = 16;
    static final int DEPTH = 16;
    private int blocks[] = new int[HEIGHT*WIDTH*DEPTH];
    private int x,y,z;

    //in chunk coords
    public Chunk initChunk(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    //local coords
    public int getBlock(int x, int y, int z) {
        return blocks[x+y*WIDTH+z*WIDTH*HEIGHT];
    }

    //local coords
    public Chunk setBlock(int block, int x, int y, int z) {
        blocks[x+y*WIDTH+z*WIDTH*HEIGHT] = block;
        return this;
    }
}
