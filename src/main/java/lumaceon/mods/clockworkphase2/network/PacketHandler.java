package lumaceon.mods.clockworkphase2.network;

import lumaceon.mods.clockworkphase2.lib.Reference;
import lumaceon.mods.clockworkphase2.network.message.*;
import lumaceon.mods.clockworkphase2.network.message.handler.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void init()
    {
        //Note: the side passed in is the RECEIVING side.
        int nextID = 0;
        INSTANCE.registerMessage(HandlerAchivementScore.class, MessageAchievementScore.class, nextID, Side.CLIENT);
        nextID++;
        INSTANCE.registerMessage(HandlerCelestialCompassItemGet.class, MessageCelestialCompassItemGet.class, nextID, Side.CLIENT);
        nextID++;
        INSTANCE.registerMessage(HandlerParticleSpawn.class, MessageParticleSpawn.class, nextID, Side.CLIENT);
        nextID++;
        INSTANCE.registerMessage(HandlerMainspringButton.class, MessageMainspringButton.class, nextID, Side.SERVER);
        nextID++;
        INSTANCE.registerMessage(HandlerToolUpgradeActivate.class, MessageToolUpgradeActivate.class, nextID, Side.SERVER);
    }
}
