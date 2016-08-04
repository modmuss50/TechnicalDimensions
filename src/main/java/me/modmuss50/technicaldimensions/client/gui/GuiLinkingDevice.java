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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 04/08/2016.
 */
public class GuiLinkingDevice extends GuiScreen {


    static DynamicTexture texture;
    static ResourceLocation location;
    public static ItemStack heldStack;

    World world;
    EntityPlayer player;

    private static final ResourceLocation backTexture = new ResourceLocation("technicaldimensions:textures/gui/linking.png");

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
        this.mc.getTextureManager().bindTexture(backTexture);
        int i = (this.width - 176) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 176, 166);

        if(location != null){
            drawTextureAt(i + 13, j + 7, location, ScreenShotUitls.clientSideImage);
        }

        if(heldStack != null){
            NBTTagCompound compound = ItemNBTHelper.getCompound(heldStack, "tpData", true);
            List<String> data = new ArrayList<>();
            if(compound != null){
                data.add("Dim: " + compound.getInteger("dim"));
                data.add("X: " + compound.getDouble("x"));
                data.add("Y: " + compound.getDouble("y"));
                data.add("Z: " + compound.getDouble("z"));
            }
            int p = 0;
            for(String str : data){
                this.fontRendererObj.drawString(str, i + 20, j + 115 + (p * 10), Color.lightGray.getRGB());
                p++;
            }
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
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
