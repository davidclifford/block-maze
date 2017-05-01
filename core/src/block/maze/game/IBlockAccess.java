package block.maze.game;

/**
 * Created by David on 14/04/2017.
 */
public interface IBlockAccess {
    int getBlock(int x, int y, int z);
    int setBlock(int block, int x, int y, int z);
}
