package me.modmuss50.technicaldimensions.client.gui;

import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.misc.CustomTeleporter;
import me.modmuss50.technicaldimensions.packets.PacketSendTPRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import reborncore.common.packets.PacketHandler;
import reborncore.common.util.ItemNBTHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Mark on 04/08/2016.
 */
public class GuiLinkingDevice extends GuiScreen {


    static DynamicTexture texture;
    static ResourceLocation location;
    public static ItemStack heldStack;

    World world;
    EntityPlayer player;

    public GuiLinkingDevice(World world, EntityPlayer player) {
        this.world = world;
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        if(ScreenShotUitls.clientSideImage != null){
            location = new ResourceLocation("RemoteImageData");
            this.texture = new DynamicTexture(ScreenShotUitls.clientSideImage.getWidth(), ScreenShotUitls.clientSideImage.getHeight());
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, this.texture);
            ScreenShotUitls.clientSideImage.getRGB(0, 0, ScreenShotUitls.clientSideImage.getWidth(), ScreenShotUitls.clientSideImage.getHeight(), this.texture.getTextureData(), 0, ScreenShotUitls.clientSideImage.getWidth());
            this.texture.updateDynamicTexture();
        } else {
            location = null;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(location != null){
          //  GL11.glScalef(4F, 4F, 0);
            drawTextureAt(10, 10, location, ScreenShotUitls.clientSideImage);
        }
    }

    public void drawTextureAt(int x, int y, ResourceLocation resourceLocation, BufferedImage texture)
    {
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
        if(heldStack != null){
            PacketHandler.sendPacketToServer(new PacketSendTPRequest(heldStack, world, player));
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
