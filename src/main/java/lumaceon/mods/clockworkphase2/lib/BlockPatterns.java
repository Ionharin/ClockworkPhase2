package lumaceon.mods.clockworkphase2.lib;


import lumaceon.mods.clockworkphase2.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;

public class BlockPatterns
{
    public static final BlockData[] CELESTIAL_COMPASS =
            {
                    new BlockData(5, 0, 2, EnumFacing.WEST.ordinal()),
                    new BlockData(5, 0, 1, EnumFacing.WEST.ordinal()),
                    new BlockData(5, 0, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(5, 0, -1, EnumFacing.WEST.ordinal()),
                    new BlockData(5, 0, -2, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, 3, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, 2, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, 1, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, -1, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, -2, EnumFacing.WEST.ordinal()),
                    new BlockData(4, 0, -3, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, 4, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, 3, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, 2, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, 1, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, -1, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, -2, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, -3, EnumFacing.WEST.ordinal()),
                    new BlockData(3, 0, -4, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, 5, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, 4, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, 3, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, 2, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, 1, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, -1, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, -2, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, -3, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, -4, EnumFacing.WEST.ordinal()),
                    new BlockData(2, 0, -5, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 5, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 4, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 3, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 2, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 1, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, -1, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, -2, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, -3, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, -4, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, -5, EnumFacing.WEST.ordinal()),
                    new BlockData(0, 0, 5, EnumFacing.NORTH.ordinal()),
                    new BlockData(0, 0, 4, EnumFacing.NORTH.ordinal()),
                    new BlockData(0, 0, 3, EnumFacing.NORTH.ordinal()),
                    new BlockData(0, 0, 2, EnumFacing.NORTH.ordinal()),
                    new BlockData(0, 0, 1, EnumFacing.NORTH.ordinal()),
                    //new BlockData(0, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(0, 0, -1, EnumFacing.SOUTH.ordinal()),
                    new BlockData(0, 0, -2, EnumFacing.SOUTH.ordinal()),
                    new BlockData(0, 0, -3, EnumFacing.SOUTH.ordinal()),
                    new BlockData(0, 0, -4, EnumFacing.SOUTH.ordinal()),
                    new BlockData(0, 0, -5, EnumFacing.SOUTH.ordinal()),
                    new BlockData(-1, 0, 5, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, 4, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, 3, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, 2, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, 1, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, -1, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, -2, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, -3, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, -4, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, 0, -5, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 5, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 4, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 3, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 2, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 1, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, -1, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, -2, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, -3, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, -4, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, -5, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, 4, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, 3, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, 2, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, 1, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, -1, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, -2, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, -3, EnumFacing.EAST.ordinal()),
                    new BlockData(-3, 0, -4, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, 3, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, 2, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, 1, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, -1, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, -2, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, 0, -3, EnumFacing.EAST.ordinal()),
                    new BlockData(-5, 0, 2, EnumFacing.EAST.ordinal()),
                    new BlockData(-5, 0, 1, EnumFacing.EAST.ordinal()),
                    new BlockData(-5, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-5, 0, -1, EnumFacing.EAST.ordinal()),
                    new BlockData(-5, 0, -2, EnumFacing.EAST.ordinal()),
            };

    public static final BlockData[] TDA = //Calculated from the top, but starts from the bottom. Translate +14Y.
            {
                    //new BlockData(0, -14, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-1, -14, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(1, -14, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-2, -14, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(2, -14, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-2, -13, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(2, -13, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-3, -13, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(3, -13, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-4, -13, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(4, -13, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-4, -12, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(4, -12, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-5, -12, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(5, -12, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-5, -11, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(5, -11, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-6, -11, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(6, -11, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-6, -10, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(6, -10, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-6, -9, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(6, -9, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-7, -9, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(7, -9, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(-7, -8, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(7, -8, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-7, -7, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(7, -7, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-7, -6, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(7, -6, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-7, -5, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(7, -5, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-6, -5, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(6, -5, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-6, -4, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(6, -4, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-6, -3, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(6, -3, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-5, -3, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(5, -3, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-5, -2, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(5, -2, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-4, -2, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(4, -2, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-4, -1, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(4, -1, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-3, -1, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(3, -1, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, -1, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(2, -1, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(-2, 0, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(2, 0, 0, EnumFacing.DOWN.ordinal()),
                    new BlockData(-1, 0, 0, EnumFacing.WEST.ordinal()),
                    new BlockData(1, 0, 0, EnumFacing.EAST.ordinal()),
                    new BlockData(0, 0, 0, EnumFacing.WEST.ordinal())
            };

    public static class BlockData
    {
        public byte x, y, z;
        public byte meta;
        public ModBlocks.BlockReference block = null;

        public BlockData(int x, int y, int z, int direction)
        {
            this.x = (byte)x;
            this.y = (byte)y;
            this.z = (byte)z;
            this.meta = (byte)direction;
        }

        public BlockData(int x, int y, int z, int direction, ModBlocks.BlockReference block)
        {
            this.x = (byte)x;
            this.y = (byte)y;
            this.z = (byte)z;
            this.meta = (byte)direction;
            this.block = block;
        }

        public ModBlocks.BlockReference getBlock() {
            return block;
        }
    }
}
