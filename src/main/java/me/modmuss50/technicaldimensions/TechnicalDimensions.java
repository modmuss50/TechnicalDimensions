package me.modmuss50.technicaldimensions;

import me.modmuss50.technicaldimensions.init.ModItems;
import me.modmuss50.technicaldimensions.misc.GuiHandler;
import me.modmuss50.technicaldimensions.packets.PacketSaveSS;
import me.modmuss50.technicaldimensions.packets.PacketSendTPRequest;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import reborncore.common.packets.AddDiscriminatorEvent;

@Mod(name = "TechnicalDimensions", modid = "technicaldimensions", version = "0.0.0")
public class TechnicalDimensions {

    @Mod.Instance
    public static TechnicalDimensions instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.load();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @SubscribeEvent
    public void registerPackets(AddDiscriminatorEvent event) {
        event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketSaveSS.class);
        event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketSendTPRequest.class);
    }


}
