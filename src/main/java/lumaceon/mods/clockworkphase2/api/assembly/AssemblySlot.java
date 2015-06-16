package lumaceon.mods.clockworkphase2.api.assembly;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Used in IAssemblable to assemble different items, like the clockwork tools.
 */
public class AssemblySlot
{
    private ItemStack itemInSlot;
    private ResourceLocation defaultTexture;
    public boolean isEnabled = true;

    //The center position of this slot. {0, 0} is considered the top-left corner while {1, 1} is the bottom right.
    public float centerX;
    public float centerY;

    //The size (diameter) of this slot. 1 is considered 1 pixel, or 1/16th of the distance from top to bottom.
    public float sizeX = 1.5F;
    public float sizeY = 1.5F;

    //The number of ticks the mouse has been over this slot, used in rendering code.
    public int ticksMousedOver = 0;

    public AssemblySlot(ResourceLocation defaultTexture)
    {
        this.defaultTexture = defaultTexture;
    }

    public AssemblySlot(ResourceLocation defaultTexture, float centerX, float centerY)
    {
        this(defaultTexture);
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public AssemblySlot(ResourceLocation defaultTexture, float centerX, float centerY, float sizeX, float sizeY)
    {
        this(defaultTexture, centerX, centerY);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public boolean isItemValid(ItemStack itemToInsert) {
        return true;
    }

    /**
     * @return The icon that will be rendered while there is no item in this slot.
     */
    public ResourceLocation getRenderIcon() {
        return defaultTexture;
    }

    public ItemStack getItemStack() {
        return itemInSlot;
    }

    public void setItemStack(ItemStack item) {
        itemInSlot = item;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
