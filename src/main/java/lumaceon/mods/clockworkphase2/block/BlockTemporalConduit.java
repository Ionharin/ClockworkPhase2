package lumaceon.mods.clockworkphase2.block;

import lumaceon.mods.clockworkphase2.tile.temporal.TileTemporalConduit;
import lumaceon.mods.clockworkphase2.util.TemporalConduitNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTemporalConduit extends BlockClockworkPhase implements ITileEntityProvider
{
    public BlockTemporalConduit(Material blockMaterial, String unlocalizedName) {
        super(blockMaterial, unlocalizedName);
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int p_149714_5_)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileTemporalConduit)
            ((TileTemporalConduit) te).updateNetwork(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileTemporalConduit)
            ((TileTemporalConduit) te).updateConnections(x, y, z);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileTemporalConduit)
            ((TileTemporalConduit) te).updateConnections(x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        super.breakBlock(world, x, y, z, block, meta);

        TileEntity te;
        TemporalConduitNetwork previousTCN = null;
        te = world.getTileEntity(x + 1, y, z);
        if(te != null && te instanceof TileTemporalConduit)
            previousTCN = ((TileTemporalConduit) te).updateNetwork(world, x + 1, y, z);

        te = world.getTileEntity(x - 1, y, z);
        if(te != null && te instanceof TileTemporalConduit)
        {
            if(previousTCN != null && ((TileTemporalConduit) te).TCN.equals(previousTCN))
                ((TileTemporalConduit) te).updateConnections(x - 1, y, z);
            else
                previousTCN = ((TileTemporalConduit) te).updateNetwork(world, x - 1, y, z);
        }

        te = world.getTileEntity(x, y + 1, z);
        if(te != null && te instanceof TileTemporalConduit)
        {
            if(previousTCN != null && ((TileTemporalConduit) te).TCN.equals(previousTCN))
                ((TileTemporalConduit) te).updateConnections(x, y + 1, z);
            else
                ((TileTemporalConduit) te).updateNetwork(world, x, y + 1, z);
        }

        te = world.getTileEntity(x, y - 1, z);
        if(te != null && te instanceof TileTemporalConduit)
        {
            if(previousTCN != null && ((TileTemporalConduit) te).TCN.equals(previousTCN))
                ((TileTemporalConduit) te).updateConnections(x, y - 1, z);
            else
                ((TileTemporalConduit) te).updateNetwork(world, x, y - 1, z);
        }

        te = world.getTileEntity(x, y, z - 1);
        if(te != null && te instanceof TileTemporalConduit)
        {
            if(previousTCN != null && ((TileTemporalConduit) te).TCN.equals(previousTCN))
                ((TileTemporalConduit) te).updateConnections(x, y, z - 1);
            else
                ((TileTemporalConduit) te).updateNetwork(world, x, y, z - 1);
        }

        te = world.getTileEntity(x, y, z + 1);
        if(te != null && te instanceof TileTemporalConduit)
        {
            if(previousTCN != null && ((TileTemporalConduit) te).TCN.equals(previousTCN))
                ((TileTemporalConduit) te).updateConnections(x, y, z + 1);
            else
                ((TileTemporalConduit) te).updateNetwork(world, x, y, z + 1);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileTemporalConduit();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}