package lumaceon.mods.clockworkphase2.client.tesr;

import lumaceon.mods.clockworkphase2.block.BlockAssemblyTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TESRTimezoneModulator extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        IBlockState state = te.getBlockType().getStateFromMeta(te.getBlockMetadata());
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        switch(state.getValue(BlockAssemblyTable.FACING).getHorizontalIndex())
        {
            case 0: //EAST
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                break;
            case 1: //SOUTH
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                break;
            case 2: //WEST
                GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
                break;
        }
        GL11.glTranslatef(0F, -1F, 0F);
        //if(state.getValue(BlockAssemblyTable.PART) == BlockAssemblyTable.EnumPartType.LEFT)
            //MODEL.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
        //else
            //MODEL_LEFT.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);

        GL11.glPopMatrix();
    }
}
