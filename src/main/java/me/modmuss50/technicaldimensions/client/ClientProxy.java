package me.modmuss50.technicaldimensions.client;

import me.modmuss50.technicaldimensions.CommonProxy;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Mark on 05/08/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
    }

    @Override
    public void setInuseStack(ItemStack stack) {
        super.setInuseStack(stack);
        GuiLinkingDevice.heldStack = stack;
    }
}
