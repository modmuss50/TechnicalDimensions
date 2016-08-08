package me.modmuss50.technicaldimensions.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Mark on 05/08/2016.
 */
public class GuiDimDevice extends GuiScreen {

    World world;
    EntityPlayer player;

    private static final ResourceLocation backTexture = new ResourceLocation("technicaldimensions:textures/gui/linking.png");

    public GuiDimDevice(World world, EntityPlayer player) {
        this.world = world;
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(backTexture);
        int i = (this.width - 176) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 176, 166);

    }

    public void drawTextureAt(int x, int y, ResourceLocation resourceLocation, BufferedImage texture) {
        this.mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, texture.getWidth(), texture.getHeight(), texture.getWidth(), texture.getHeight());
        GlStateManager.disableBlend();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//        if (heldStack != null) {
//            NBTTagCompound compound = ItemNBTHelper.getCompound(heldStack, "tpData", true);
//            if (compound != null) {
//                int dimID = compound.getInteger("dim");
//                if (dimID == player.worldObj.provider.getDimension() && !allowIntraTravel) {
//                    return;
//                }
//            }
//            PacketHandler.sendPacketToServer(new PacketSendTPRequest(heldStack, world, player));
//            Minecraft.getMinecraft().displayGuiScreen(null);
//        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
