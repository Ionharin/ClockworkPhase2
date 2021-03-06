package lumaceon.mods.clockworkphase2.item;

import lumaceon.mods.clockworkphase2.tile.machine.TileClockworkMachine;
import lumaceon.mods.clockworkphase2.tile.temporal.TileSimpleOverclocker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class ItemBugSwatter extends ItemClockworkPhase
{
    public ItemBugSwatter(int maxStack, int maxDamage, String unlocalizedName) {
        super(maxStack, maxDamage, unlocalizedName);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te != null && te instanceof TileClockworkMachine)
        {
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            {
                ((TileClockworkMachine) te).toggleAntiMode();
            }
            else
            {
                ((TileClockworkMachine) te).energyStorage.receiveEnergy(10000, false);
            }
        }
        if(te != null && te instanceof TileSimpleOverclocker)
        {
            System.out.println(((TileSimpleOverclocker) te).getTimezone().getTimeInTicks());
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack is, World world, EntityPlayer player, EnumHand hand)
    {
        return ActionResult.newResult(EnumActionResult.SUCCESS, is);
    }
}
