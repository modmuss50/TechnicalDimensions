package me.modmuss50.technicaldimensions.client;

import me.modmuss50.technicaldimensions.CommonProxy;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import me.modmuss50.technicaldimensions.client.render.RenderPortal;
import me.modmuss50.technicaldimensions.tiles.TilePortal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Mark on 05/08/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortal.class, new RenderPortal());
    }

    @Override
    public void setInuseStack(ItemStack stack) {
        super.setInuseStack(stack);
        GuiLinkingDevice.heldStack = stack;
    }
}
