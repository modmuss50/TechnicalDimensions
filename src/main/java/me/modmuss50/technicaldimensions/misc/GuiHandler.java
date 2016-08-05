package me.modmuss50.technicaldimensions.misc;

import me.modmuss50.technicaldimensions.client.gui.GuiDimDevice;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Mark on 04/08/2016.
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            return new GuiLinkingDevice(world, player);
        }
        if (ID == 1) {
            return new GuiDimDevice(world, player);
        }
        return null;
    }
}
