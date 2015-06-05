package lumaceon.mods.clockworkphase2.tile.generic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Credits to Azanor for the packet handling code.
 */
public abstract class TileClockworkPhase extends TileEntity
{
    public abstract void setState(byte state);

    public void writeCustomNBT(NBTTagCompound nbt) {
        writeToNBT(nbt);
    }

    public void readCustomNBT(NBTTagCompound nbt) {
        readFromNBT(nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeCustomNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readCustomNBT(pkt.func_148857_g());
    }
}