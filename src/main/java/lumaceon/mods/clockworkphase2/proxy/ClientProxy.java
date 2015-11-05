package lumaceon.mods.clockworkphase2.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import lumaceon.mods.clockworkphase2.client.ClientTickHandler;
import lumaceon.mods.clockworkphase2.client.render.RenderHandler;
import lumaceon.mods.clockworkphase2.client.render.elements.world.WorldRenderElement;
import lumaceon.mods.clockworkphase2.client.render.elements.world.WorldRenderElementTDA;
import lumaceon.mods.clockworkphase2.client.render.elements.world.WorldRenderElementTemporalClock;
import lumaceon.mods.clockworkphase2.client.render.elements.world.WorldRenderElementTemporalDisplacementAltar;
import lumaceon.mods.clockworkphase2.client.render.sky.SkyRendererForthAge;
import lumaceon.mods.clockworkphase2.client.tesr.*;
import lumaceon.mods.clockworkphase2.handler.WorldHandler;
import lumaceon.mods.clockworkphase2.tile.TileAssemblyTable;
import lumaceon.mods.clockworkphase2.tile.TileAssemblyTableSB;
import lumaceon.mods.clockworkphase2.tile.TileTelescope;
import lumaceon.mods.clockworkphase2.tile.temporal.TileTemporalFurnace;
import lumaceon.mods.clockworkphase2.tile.machine.TileTimezoneFluidExporter;
import lumaceon.mods.clockworkphase2.tile.temporal.TileTemporalConduit;
import lumaceon.mods.clockworkphase2.world.provider.forthage.WorldProviderForthAge;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerTESR()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileTemporalConduit.class, new TESRTemporalConduit());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAssemblyTable.class, new TESRAssemblyTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAssemblyTableSB.class, new TESRAssemblyTableSB());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTimezoneFluidExporter.class, new TESRTimezoneFluidExporter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTelescope.class, new TESRTelescope());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTemporalFurnace.class, new TESRTemporalFurnace());
    }

    @Override
    public void registerModels() {}

    @Override
    public void registerKeybindings() {}

    @Override
    public void initSideHandlers()
    {
        RenderHandler renderer = new RenderHandler();

        MinecraftForge.EVENT_BUS.register(renderer);
        FMLCommonHandler.instance().bus().register(renderer);
        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
    }

    @Override
    public void addWorldRenderer(World world, int x, int y, int z, int ID)
    {
        switch(ID)
        {
            case 0: //Temporal machine clock renderer.
                RenderHandler.registerWorldRenderElement(new WorldRenderElementTemporalClock(world, x, y, z));
                break;
            case 1: //Temporal displacement altar renderer.
                RenderHandler.registerWorldRenderElement(new WorldRenderElementTemporalDisplacementAltar(world, x, y, z));
                break;
            case 2://TDA Stargate style
                RenderHandler.registerWorldRenderElement(new WorldRenderElementTDA(world, x, y, z));
                break;
        }
    }

    @Override
    public void clearWorldRenderers(World world, int x, int y, int z)
    {
        for(int n = 0; n < RenderHandler.worldRenderList.size(); n++)
        {
            WorldRenderElement wre = RenderHandler.worldRenderList.get(n);
            if(wre.isFinished())
            {
                RenderHandler.worldRenderList.remove(n);
                --n;
            }
        }
    }

    @Override
    public IRenderHandler getSkyRendererForWorld(WorldProvider worldProvider) {
        if(worldProvider instanceof WorldProviderForthAge)
            return new SkyRendererForthAge();
        return null;
    }
}
