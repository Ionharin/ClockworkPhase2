package lumaceon.mods.clockworkphase2.tile.generic;

import lumaceon.mods.clockworkphase2.api.timezone.ITimeSandTile;
import lumaceon.mods.clockworkphase2.api.timezone.ITimezone;
import lumaceon.mods.clockworkphase2.api.timezone.TimezoneHandler;
import lumaceon.mods.clockworkphase2.api.util.TimeConverter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileTimeSandInventoryMachine extends TileClockworkPhaseInventory implements ITimeSandTile
{
    public ITimezone timezone;
    protected int tz_x, tz_y, tz_z;

    protected long internalTimeSand;
    protected long timeSandRequestAmount;

    protected int timeBetweenTimezoneRequests = 100;
    protected int timeRequestTimer = timeBetweenTimezoneRequests;

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setLong("internal_time_sand", internalTimeSand);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        internalTimeSand = nbt.getLong("internal_time_sand");
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(timeRequestTimer > 0)
            timeRequestTimer--;
    }

    @Override
    public long getTimeSandMax() {
        return TimeConverter.DAY;
    }

    @Override
    public long getTimeSand() {
        return internalTimeSand;
    }

    @Override
    public long addTimeSand(long timeSand)
    {
        if(timeSand > getTimeSandMax() - internalTimeSand)
        {
            long amountAdded = getTimeSandMax() - internalTimeSand;
            internalTimeSand = getTimeSandMax();
            return amountAdded;
        }
        else
        {
            internalTimeSand += timeSand;
            return timeSand;
        }
    }

    @Override
    public long consumeTimeSand(long timeSand)
    {
        if(timeSand > internalTimeSand)
        {
            long amountConsumed = internalTimeSand;
            internalTimeSand = 0;
            return amountConsumed;
        }
        else
        {
            internalTimeSand -= timeSand;
            return timeSand;
        }
    }

    public ITimezone getTimezone()
    {
        if(timezone != null)
            return timezone;
        TileEntity te = worldObj.getTileEntity(tz_x, tz_y, tz_z);
        if(te != null && te instanceof ITimezone)
        {
            timezone = (ITimezone) te;
            return timezone;
        }

        ITimezone timezone = TimezoneHandler.getTimeZone(xCoord, yCoord, zCoord, worldObj);
        if(timezone != null)
            this.timezone = timezone;
        return timezone;
    }

    public boolean requestTimeSandFromTimezone(World world, int x, int y, int z, long timeSandToRequest)
    {
        if(timeRequestTimer <= 0)
        {
            timeRequestTimer = timeBetweenTimezoneRequests;
            ITimezone timezone = getTimezone();

            if(timezone == null)
                return false;

            //Requests to consume, adds request minus what couldn't be consumed to machine, adds back any overspill.
            addTimeSand(timezone.consumeTimeSand(timeSandToRequest));
        }
        return true;
    }
}
