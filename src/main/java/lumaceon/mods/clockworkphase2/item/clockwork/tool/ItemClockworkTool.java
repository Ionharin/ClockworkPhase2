package lumaceon.mods.clockworkphase2.item.clockwork.tool;

import lumaceon.mods.clockworkphase2.ClockworkPhase2;
import lumaceon.mods.clockworkphase2.api.assembly.ContainerAssemblyTable;
import lumaceon.mods.clockworkphase2.api.assembly.IAssemblable;
import lumaceon.mods.clockworkphase2.api.capabilities.EnergyStorageModular;
import lumaceon.mods.clockworkphase2.api.capabilities.ItemStackHandlerClockworkConstruct;
import lumaceon.mods.clockworkphase2.api.item.clockwork.IClockwork;
import lumaceon.mods.clockworkphase2.api.util.*;
import lumaceon.mods.clockworkphase2.config.ConfigValues;
import lumaceon.mods.clockworkphase2.init.ModItems;
import lumaceon.mods.clockworkphase2.inventory.slot.SlotItemSpecific;
import lumaceon.mods.clockworkphase2.item.temporal.excavator.ItemToolUpgradeArea;
import lumaceon.mods.clockworkphase2.util.ISimpleNamed;
import lumaceon.mods.clockworkphase2.util.NBTTags;
import lumaceon.mods.clockworkphase2.lib.Textures;
import lumaceon.mods.clockworkphase2.util.NBTHelper;
import lumaceon.mods.clockworkphase2.util.RayTraceHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Credits to the Tinker's Construct developers for a lot of the AOE code.
 */
public abstract class ItemClockworkTool extends ItemTool implements IAssemblable, ISimpleNamed, IClockwork
{
    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;
    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    String simpleName;

    public ItemClockworkTool(float var1, ToolMaterial toolMaterial, Set set, String name) {
        super(var1, 1, toolMaterial, set);
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
        this.setCreativeTab(ClockworkPhase2.instance.CREATIVE_TAB);
        this.simpleName = name;
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag)
    {
        IEnergyStorage energyCap = is.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
        if(energyCap != null)
        {
            list.add("Energy: " + energyCap.getEnergyStored() + " fe");
            list.add("");
        }

        ItemStackHandlerClockworkConstruct cap = getClockworkItemHandler(is);
        if(cap != null)
        {
            list.add("Quality: " + cap.getQuality());
            list.add("Speed: " + cap.getSpeed());
            list.add("Tier: " + cap.getTier());
        }
        //InformationDisplay.addClockworkConstructInformation(is, player, list, true);
    }

    public int getQuality(ItemStack item) {
        return ClockworkHelper.getQuality(item);
    }

    public int getSpeed(ItemStack item) {
        return ClockworkHelper.getSpeed(item);
    }
    public int getTier(ItemStack item) {
        return ClockworkHelper.getTier(item);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        int harvestLevel = -1;

        if(toolClass.equals(this.getHarvestType()))
            return this.getTier(stack);

        return harvestLevel;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = player.worldObj.getBlockState(pos);
        if(state == null || !isEffective(state)) //The tool is ineffective or non-functional.
            return super.onBlockStartBreak(stack, pos, player);

        boolean found = false;
        int areaRadius = 1;
        ItemStack[] components = NBTHelper.INVENTORY.get(stack, NBTTags.COMPONENT_INVENTORY);
        if(components != null)
            for(ItemStack item : components) //Loop through the tool's component inventory.
                if(item != null && item.getItem() instanceof ItemToolUpgradeArea && ((ItemToolUpgradeArea) item.getItem()).getActive(item, stack))
                {
                    areaRadius = ((ItemToolUpgradeArea) item.getItem()).getAreaRadius(item);
                    found = true;
                    break;
                }
        if(!found) //No area upgrade found.
            return super.onBlockStartBreak(stack, pos, player);

        RayTraceResult mop = RayTraceHelper.rayTrace(player.worldObj, player, false, 4.5);
        if(mop == null)
            return super.onBlockStartBreak(stack, pos, player);

        int xRadius = areaRadius;
        int yRadius = areaRadius;
        int zRadius = 1;
        switch(mop.sideHit.getIndex())
        {
            case 0:
            case 1:
                yRadius = 1; //DEPTH
                zRadius = areaRadius;
                break;
            case 2:
            case 3:
                xRadius = areaRadius;
                zRadius = 1; //DEPTH
                break;
            case 4:
            case 5:
                xRadius = 1; //DEPTH
                zRadius = areaRadius;
                break;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for(int x1 = x - xRadius + 1; x1 <= x + xRadius - 1; x1++)
            for(int y1 = y - yRadius + 1; y1 <= y + yRadius - 1; y1++)
                for(int z1 = z - zRadius + 1; z1 <= z + zRadius - 1; z1++)
                {
                    if((x1 == x && y1 == y && z1 == z) || super.onBlockStartBreak(stack, new BlockPos(x1, y1, z1), player))
                        continue;
                    aoeBlockBreak(stack, player.worldObj, new BlockPos(x1, y1, z1), player);
                }
        return false;
    }

    private void aoeBlockBreak(ItemStack stack, World world, BlockPos pos, EntityPlayer player)
    {
        if(world.isAirBlock(pos))
            return;

        if(!(player instanceof EntityPlayerMP))
            return;
        EntityPlayerMP playerMP = (EntityPlayerMP) player;

        IBlockState state = world.getBlockState(pos);
        if(!isEffective(state) || !ForgeHooks.canHarvestBlock(state.getBlock(), player, world, pos))
            return;

        int event = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos);
        if(event == -1)
            return;

        stack.onBlockDestroyed(world, state, pos, player);
        if(!world.isRemote)
        {
            state.getBlock().onBlockHarvested(world, pos, state, player);
            if(state.getBlock().removedByPlayer(state, world, pos, player, true))
            {
                state.getBlock().onBlockDestroyedByPlayer(world, pos, state);
                state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
                state.getBlock().dropXpOnBlockBreak(world, pos, event);
            }
            //playerMP.playerNetServerHandler.sendPacket(new SPacketBlockChange(world, pos));
            //TODO fix
        }
        else //CLIENT
        {
            //world.playAuxSFX(2001, pos, Block.getIdFromBlock(block) + (metadata << 12));
            if(state.getBlock().removedByPlayer(state, world, pos, player, true))
                state.getBlock().onBlockDestroyedByPlayer(world, pos, state);
            ItemStack itemstack = player.getActiveItemStack();
            if(itemstack != null)
            {
                itemstack.onBlockDestroyed(world, state, pos, player);
                //if(itemstack.stackSize == 0)
                //    player.destroyCurrentEquippedItem();
            }
            //Minecraft.getMinecraft().getNetHandler().addToSendQueue(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
            //TODO fix
        }
    }

    @Override
    public float getStrVsBlock(ItemStack is, IBlockState state)
    {
        boolean correctMaterial = false;
        for(Material mat : getEffectiveMaterials())
            if(mat.equals(state.getMaterial()))
                correctMaterial = true;

        if(!correctMaterial)
            return 1.0F;

        IEnergyStorage energyStorage = is.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
        if(energyStorage == null)
            return 1.0F;

        int energy = energyStorage.getEnergyStored();
        if(energy <= 0)
            return 0.0F;

        int speed = getSpeed(is);
        if(speed <= 0)
            return 0.0F;

        return (float) speed / 25;
    }

    public abstract String getHarvestType();
    public abstract Material[] getEffectiveMaterials();

    public boolean isEffective(IBlockState state) {
        return this.getHarvestType().equals(state.getBlock().getHarvestTool(state)) || isEffective(state.getMaterial());
    }

    public boolean isEffective(Material material) {
        for(Material m : getEffectiveMaterials())
            if(m == material)
                return true;
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack is, World world, IBlockState state, BlockPos pos, EntityLivingBase playerIn)
    {
        if(state.getBlockHardness(world, pos) <= 0)
            return true;

        if(!isEffective(state))
            return true;

        IEnergyStorage energyStorage = is.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
        if(energyStorage == null)
            return true;

        int currentEnergy = energyStorage.getEnergyStored();
        if(currentEnergy <= 0)
            return true;

        int quality = getQuality(is);
        int speed = getSpeed(is);
        int tensionCost = ClockworkHelper.getTensionCostFromStats(ConfigValues.BASE_TENSION_COST_PER_BLOCK_BREAK, quality, speed);

        energyStorage.extractEnergy(tensionCost, false);

        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack));
    }

    @Override
    public String getUnlocalizedName() {
        return this.getRegistryName().toString();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public ResourceLocation getGUIBackground(ContainerAssemblyTable container) {
        return Textures.GUI.ASSEMBLY_TABLE_CONSTRUCT;
    }

    @Override
    public Slot[] getContainerSlots(IInventory inventory)
    {
        return new Slot[]
                {
                        new SlotItemSpecific(inventory, 0, 160, 41, ModItems.mainspring),
                        new SlotItemSpecific(inventory, 1, 125, 41, ModItems.clockworkCore)
                };
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ClockworkToolCapabilityProvider(stack);
    }

    private ItemStackHandlerClockworkConstruct getClockworkItemHandler(ItemStack is)
    {
        IItemHandler itemHandler = is.getCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        if(itemHandler != null && itemHandler instanceof ItemStackHandlerClockworkConstruct)
            return (ItemStackHandlerClockworkConstruct) itemHandler;
        return null;
    }

    private static class ClockworkToolCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
    {
        @CapabilityInject(IEnergyStorage.class)
        static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;
        @CapabilityInject(IItemHandler.class)
        static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

        EnergyStorageModular energyStorage;
        ItemStackHandlerClockworkConstruct inventory;

        public ClockworkToolCapabilityProvider(ItemStack stack) {
            inventory = new ItemStackHandlerClockworkConstruct(2, stack);
            energyStorage = new EnergyStorageModular(1);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability != null && capability == ENERGY_STORAGE_CAPABILITY || capability == ITEM_HANDLER_CAPABILITY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
        {
            if(capability == null)
                return null;

            if(capability == ENERGY_STORAGE_CAPABILITY)
                return ENERGY_STORAGE_CAPABILITY.cast(energyStorage);
            else if(capability == ITEM_HANDLER_CAPABILITY)
                return ITEM_HANDLER_CAPABILITY.cast(inventory);

            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("inventory", inventory.serializeNBT());
            tag.setInteger("energy", energyStorage.getEnergyStored());
            tag.setInteger("max_capacity", energyStorage.getMaxEnergyStored());
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            inventory.deserializeNBT((NBTTagCompound) nbt.getTag("inventory"));
            energyStorage.setMaxCapacity(nbt.getInteger("max_capacity"));
            energyStorage.receiveEnergy(nbt.getInteger("energy"), false);
        }
    }
}
