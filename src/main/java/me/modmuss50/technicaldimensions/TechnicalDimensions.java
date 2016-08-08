package me.modmuss50.technicaldimensions;

import me.modmuss50.technicaldimensions.blocks.BreakBlockEvent;
import me.modmuss50.technicaldimensions.command.CommandTPX;
import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.init.ModItems;
import me.modmuss50.technicaldimensions.init.ModRecipes;
import me.modmuss50.technicaldimensions.misc.GuiHandler;
import me.modmuss50.technicaldimensions.misc.TeleportationUtils;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.server.ServerScreenShotUtils;
import me.modmuss50.technicaldimensions.world.ModDimensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.IOException;

@Mod(name = "TechnicalDimensions", modid = "technicaldimensions", version = "@MODVERSION@", dependencies = "required-after:reborncore")
public class TechnicalDimensions {

    @Mod.Instance
    public static TechnicalDimensions instance;

    @SidedProxy(clientSide = "me.modmuss50.technicaldimensions.client.ClientProxy", serverSide = "me.modmuss50.technicaldimensions.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.load();
        ModBlocks.load();
        ModRecipes.load();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ServerScreenShotUtils.class);
        MinecraftForge.EVENT_BUS.register(BreakBlockEvent.class);
        MinecraftForge.EVENT_BUS.register(TeleportationUtils.class);
        PacketUtill.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        proxy.init();
    }

    @Mod.EventHandler
    public void severStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTPX());
    }

    @Mod.EventHandler
    public void worldLoad(FMLServerStartedEvent serverStartedEvent) {
        try {
            ModDimensions.worldLoad(serverStartedEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void unloadAll(FMLServerStoppingEvent serverStoppingEvent) {
        ModDimensions.unloadAll(serverStoppingEvent);
    }


}
