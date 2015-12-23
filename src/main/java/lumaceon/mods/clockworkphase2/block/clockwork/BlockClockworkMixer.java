package lumaceon.mods.clockworkphase2.block.clockwork;

import lumaceon.mods.clockworkphase2.block.BlockClockworkPhase;
import lumaceon.mods.clockworkphase2.tile.clockwork.TileClockworkMixer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockClockworkMixer extends BlockClockworkPhase implements ITileEntityProvider
{
    public BlockClockworkMixer(Material blockMaterial, String unlocalizedName) {
        super(blockMaterial, unlocalizedName);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileClockworkMixer();
    }
}
