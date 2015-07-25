package lumaceon.mods.clockworkphase2.item;

import lumaceon.mods.clockworkphase2.api.phase.Phase;
import lumaceon.mods.clockworkphase2.api.phase.Phases;
import lumaceon.mods.clockworkphase2.extendeddata.ExtendedPlayerProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBugSwatter extends ItemClockworkPhase
{
    public ItemBugSwatter(int maxStack, int maxDamage, String unlocalizedName) {
        super(maxStack, maxDamage, unlocalizedName);
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int meta, float f1, float f2, float f3)
    {
        System.out.println(ExtendedPlayerProperties.get(player).temporalInfluence);
        Phase[] phases = Phases.getPhases(world, x, y, z);
        for(Phase phase : phases)
            System.out.println(phase.toString());
        return true;
    }
}
