package lumaceon.mods.clockworkphase2.item.temporal.excavator;

import com.google.common.collect.Sets;
import lumaceon.mods.clockworkphase2.ClockworkPhase2;
import lumaceon.mods.clockworkphase2.api.assembly.ContainerAssemblyTable;
import lumaceon.mods.clockworkphase2.api.assembly.IAssemblable;
import lumaceon.mods.clockworkphase2.api.capabilities.EnergyStorageModular;
import lumaceon.mods.clockworkphase2.api.item.IKeybindActivation;
import lumaceon.mods.clockworkphase2.api.item.clockwork.IClockwork;
import lumaceon.mods.clockworkphase2.api.util.ClockworkHelper;
import lumaceon.mods.clockworkphase2.api.util.HourglassHelper;
import lumaceon.mods.clockworkphase2.api.util.InformationDisplay;
import lumaceon.mods.clockworkphase2.lib.GUIs;
import lumaceon.mods.clockworkphase2.util.ISimpleNamed;
import lumaceon.mods.clockworkphase2.util.NBTHelper;
import lumaceon.mods.clockworkphase2.util.NBTTags;
import lumaceon.mods.clockworkphase2.config.ConfigValues;
import lumaceon.mods.clockworkphase2.init.ModItems;
import lumaceon.mods.clockworkphase2.inventory.slot.SlotItemSpecific;
import lumaceon.mods.clockworkphase2.inventory.slot.SlotToolUpgrade;
import lumaceon.mods.clockworkphase2.item.clockwork.tool.ItemClockworkTool;
import lumaceon.mods.clockworkphase2.lib.Textures;
import lumaceon.mods.clockworkphase2.util.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemTemporalExcavator extends ItemTool implements IAssemblable, IKeybindActivation, ISimpleNamed
{
    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    private static final Set field_150915_c = Sets.newHashSet(new Block[]{Blocks.COBBLESTONE, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB, Blocks.STONE, Blocks.SANDSTONE, Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE, Blocks.IRON_BLOCK, Blocks.COAL_ORE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK, Blocks.ICE, Blocks.NETHERRACK, Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.GRASS, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL, Blocks.SNOW_LAYER, Blocks.SNOW, Blocks.CLAY, Blocks.FARMLAND, Blocks.SOUL_SAND, Blocks.MYCELIUM, Blocks.CHORUS_PLANT, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN});
    String simpleName;

    public ItemTemporalExcavator(ToolMaterial p_i45333_2_, String name)
    {
        super(0, 1, p_i45333_2_, field_150915_c);
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
        if(is != null)
        {
            IEnergyStorage cap = is.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
            if(cap != null)
            {
                String color = InformationDisplay.getColorFromTension(cap.getEnergyStored(), cap.getMaxEnergyStored());
                list.add("Energy: " + color + cap.getEnergyStored() + "/" + cap.getMaxEnergyStored() + " fe");
            }
        }
    }

    @Override
    public float getStrVsBlock(ItemStack is, IBlockState state)
    {
        float strengthVsBlock = 0.0F;

        IEnergyStorage energyStorage = is.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
        if(energyStorage == null || energyStorage.getEnergyStored() <= 0)
            return 0.0F;

        ItemStack item;
        ItemStackHandlerTemporalExcavator inventory = getInventoryHandler(is);
        if(inventory != null)
        {
            item = inventory.getPickaxe();
            if(item != null && item.getItem() instanceof ItemClockworkTool)
                strengthVsBlock = Math.max(strengthVsBlock, item.getItem().getStrVsBlock(item, state));

            item = inventory.getAxe();
            if(item != null && item.getItem() instanceof ItemClockworkTool)
                strengthVsBlock = Math.max(strengthVsBlock, item.getItem().getStrVsBlock(item, state));

            item = inventory.getShovel();
            if(item != null && item.getItem() instanceof ItemClockworkTool)
                strengthVsBlock = Math.max(strengthVsBlock, item.getItem().getStrVsBlock(item, state));


            for(int n = 3; n < inventory.getSlots(); n++)
            {
                item = inventory.getStackInSlot(n);
                if(item != null && item.getItem() instanceof ItemToolUpgradeTemporalInfuser)
                    if(((ItemToolUpgradeTemporalInfuser) item.getItem()).getActive(item, is))
                        return 1000000000F;
            }
        }

        return strengthVsBlock;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack is, World world, IBlockState state, BlockPos pos, EntityLivingBase playerIn)
    {
        float blockHardness = state.getBlockHardness(world, pos);
        if(blockHardness <= 0)
            return true;

        ItemStack mostSpeedyTool = null;
        float greatestStrength = 0.0F;
        ItemStack component;

        ItemStackHandlerTemporalExcavator inventory = getInventoryHandler(is);
        if(inventory != null)
        {
            for(int i = 0; i < inventory.getSlots(); i++)
            {
                component = inventory.getStackInSlot(i);
                if(component != null)
                {
                    if(component.getItem() instanceof ItemToolUpgradeTemporalInfuser)
                    {
                        if(((ItemToolUpgradeTemporalInfuser) component.getItem()).getActive(component, is))
                        {
                            HourglassHelper.consumeTimeMostPossible(HourglassHelper.getActiveHourglasses((EntityPlayer) playerIn), HourglassHelper.getTimeToBreakBlock(world, pos, state, playerIn, is));
                            break;
                        }
                    }
                    if(component.getItem() instanceof ItemClockworkTool)
                    {
                        float strength = component.getItem().getStrVsBlock(component, state);
                        if(mostSpeedyTool == null || strength > greatestStrength)
                        {
                            mostSpeedyTool = component;
                            greatestStrength = strength;
                        }
                    }
                }
            }
        }

        if(greatestStrength > 1.0F && mostSpeedyTool != null && mostSpeedyTool.getItem() instanceof IClockwork)
        {
            IClockwork clockworkConstruct = (IClockwork) mostSpeedyTool.getItem();
            IEnergyStorage energyStorage = is.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
            if(energyStorage == null)
                return true;

            int currentEnergy = energyStorage.getEnergyStored();
            if(currentEnergy <= 0)
                return true;

            int quality = clockworkConstruct.getQuality(mostSpeedyTool);
            int speed = clockworkConstruct.getSpeed(mostSpeedyTool);
            int energyCost = ClockworkHelper.getTensionCostFromStats(ConfigValues.BASE_TENSION_COST_PER_BLOCK_BREAK, quality, speed);

            energyStorage.extractEnergy(energyCost, false);
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
    {
        IBlockState blockState = player.worldObj.getBlockState(pos);
        if(blockState.getBlock() == null || !isEffective(stack, blockState)) //The tool is ineffective or non-functional.
            return super.onBlockStartBreak(stack, pos, player);

        boolean found = false;
        int areaRadius = 1;
        ItemStack item;

        ItemStackHandlerTemporalExcavator inventory = getInventoryHandler(stack);
        if(inventory != null)
        {
            for(int i = 0; i < inventory.getSlots(); i++) //Loop through the tool's component inventory.
            {
                item = inventory.getStackInSlot(i);
                if(item != null && item.getItem() instanceof ItemToolUpgradeArea && ((ItemToolUpgradeArea) item.getItem()).getActive(item, stack))
                {
                    areaRadius = ((ItemToolUpgradeArea) item.getItem()).getAreaRadius(item);
                    found = true;
                    break;
                }
            }
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
        if(!isEffective(stack, state) || !ForgeHooks.canHarvestBlock(state.getBlock(), player, world, pos))
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
                    //player.destroyCurrentEquippedItem();
            }
            //Minecraft.getMinecraft().getNetHandler().addToSendQueue(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
            //TODO fix
        }
    }

    protected boolean isEffective(ItemStack is, IBlockState state)
    {
        if(is != null)
        {
            ItemStackHandlerTemporalExcavator inventory = getInventoryHandler(is);
            if(inventory != null)
            {
                ItemStack temp;

                temp = inventory.getPickaxe();
                if(temp != null && temp.getItem() instanceof ItemClockworkTool)
                    if(((ItemClockworkTool) temp.getItem()).isEffective(state))
                        return true;

                temp = inventory.getAxe();
                if(temp != null && temp.getItem() instanceof ItemClockworkTool)
                    if(((ItemClockworkTool) temp.getItem()).isEffective(state))
                        return true;

                temp = inventory.getShovel();
                if(temp != null && temp.getItem() instanceof ItemClockworkTool)
                    if(((ItemClockworkTool) temp.getItem()).isEffective(state))
                        return true;
            }
        }
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
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
    public ResourceLocation getGUIBackground(ContainerAssemblyTable container) {
        return Textures.GUI.ASSEMBLY_TABLE_TEMPORAL_EXCAVATOR;
    }

    @Override
    public Slot[] getContainerSlots(IInventory inventory) {
        return new Slot[]
                {
                        new SlotItemSpecific(inventory, 0, 106, 59, ModItems.clockworkPickaxe),
                        new SlotItemSpecific(inventory, 1, 178, 59, ModItems.clockworkAxe),
                        new SlotItemSpecific(inventory, 2, 142, 41, ModItems.clockworkShovel),
                        new SlotToolUpgrade(inventory, 3, 61, 106),
                        new SlotToolUpgrade(inventory, 4, 79, 106),
                        new SlotToolUpgrade(inventory, 5, 97, 106),
                        new SlotToolUpgrade(inventory, 6, 115, 106),
                        new SlotToolUpgrade(inventory, 7, 133, 106),
                        new SlotToolUpgrade(inventory, 8, 151, 106),
                        new SlotToolUpgrade(inventory, 9, 169, 106),
                        new SlotToolUpgrade(inventory, 10, 187, 106),
                        new SlotToolUpgrade(inventory, 11, 205, 106),
                        new SlotToolUpgrade(inventory, 12, 223, 106),
                };
    }

    @Override
    public void onKeyPressed(ItemStack item, EntityPlayer player) {
        player.openGui(ClockworkPhase2.instance, GUIs.TEMPORAL_EXCAVATOR.ordinal(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.getItem().equals(newStack.getItem());
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new TemporalExcavatorCapabilityProvider(stack);
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    public ItemStackHandlerTemporalExcavator getInventoryHandler(ItemStack item)
    {
        if(item != null)
        {
            IItemHandler handler = item.getCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if(handler != null && handler instanceof  ItemStackHandlerTemporalExcavator)
                return (ItemStackHandlerTemporalExcavator) handler;
        }
        return null;
    }

    private static class TemporalExcavatorCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
    {
        @CapabilityInject(IEnergyStorage.class)
        static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;
        @CapabilityInject(IItemHandler.class)
        static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

        EnergyStorageModular energyStorage;
        ItemStackHandlerTemporalExcavator inventory;

        private TemporalExcavatorCapabilityProvider(ItemStack stack) {
            inventory = new ItemStackHandlerTemporalExcavator(13, stack);
            energyStorage = new EnergyStorageModular(1);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability != null && capability == ITEM_HANDLER_CAPABILITY || capability == ENERGY_STORAGE_CAPABILITY;
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
        public void deserializeNBT(NBTTagCompound nbt) {
            inventory.deserializeNBT((NBTTagCompound) nbt.getTag("inventory"));
            energyStorage.setMaxCapacity(nbt.getInteger("max_capacity"));
            energyStorage.receiveEnergy(nbt.getInteger("energy"), false);
        }
    }

    public static class ItemStackHandlerTemporalExcavator extends ItemStackHandler
    {
        ItemStack stack;

        public ItemStackHandlerTemporalExcavator(int size, ItemStack stack) {
            super(size);
            this.stack = stack;
        }

        public ItemStack getPickaxe() {
            return getStackInSlot(0);
        }

        public ItemStack getAxe() {
            return getStackInSlot(1);
        }

        public ItemStack getShovel() {
            return getStackInSlot(2);
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            int maxEnergy = 0;
            for(ItemStack s : stacks)
            {
                if(s != null)
                {
                    IEnergyStorage cap = s.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
                    if(cap != null)
                    {
                        maxEnergy += cap.getMaxEnergyStored();
                        cap.extractEnergy(cap.getMaxEnergyStored(), false);
                        cap.receiveEnergy(1, false);
                    }
                }
            }

            if(stack != null)
            {
                IEnergyStorage cap = stack.getCapability(ENERGY_STORAGE_CAPABILITY, EnumFacing.DOWN);
                if(cap != null && cap instanceof EnergyStorageModular)
                {
                    ((EnergyStorageModular) cap).setMaxCapacity(maxEnergy);
                }
                stack.setItemDamage(stack.getMaxDamage());
            }
        }
    }
}
