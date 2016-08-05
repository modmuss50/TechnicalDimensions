package me.modmuss50.technicaldimensions.client.gui;

import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.misc.LinkingIDHelper;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestSSData;
import me.modmuss50.technicaldimensions.packets.teleportation.PacketSendTPRequest;
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
import reborncore.common.packets.PacketHandler;
import reborncore.common.util.ItemNBTHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mark on 04/08/2016.
 */
public class GuiLinkingDevice extends GuiScreen {

    public static ItemStack heldStack;

    public static BufferedImage image;
    static DynamicTexture texture;
    static ResourceLocation location;
    public static String currenLoadedImageID;
    public static String neededImageID;
    private static boolean hasImage = false;

    public static BufferedImage tempImage = null;
    public static String tempID = null;

    World world;
    EntityPlayer player;

    private static final ResourceLocation backTexture = new ResourceLocation("technicaldimensions:textures/gui/linking.png");

    public GuiLinkingDevice(World world, EntityPlayer player) {
        hasImage = false;
        this.world = world;
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        if (heldStack == null) {
            //Bad things have happened
        } else {
            NBTTagCompound compound = ItemNBTHelper.getCompound(heldStack, "tpData", true);
            if(compound != null){
                String imageID = compound.getString("imageID");
                if (!imageID.isEmpty()) {
                    neededImageID = imageID;
                    if (ScreenShotUitls.imageMap.containsKey(imageID)) {
                        if (ScreenShotUitls.bufferedImageMap.containsKey(imageID)) {
                            loadImage(ScreenShotUitls.bufferedImageMap.get(imageID), imageID);
                        } else {
                            try {
                                BufferedImage image = ScreenShotUitls.imageFromString(ScreenShotUitls.imageMap.get(imageID));
                                ScreenShotUitls.bufferedImageMap.put(imageID, image);
                                loadImage(image, imageID);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        PacketHandler.sendPacketToServer(new PacketRequestSSData(imageID, player));
                    }
                }
            }

        }
    }

    public static void loadImage(BufferedImage bufferedImage, String imageID) {
        ResourceLocation newLoc = new ResourceLocation(imageID);
        location = newLoc;
        texture = new DynamicTexture(bufferedImage.getWidth(), bufferedImage.getHeight());
        Minecraft.getMinecraft().getTextureManager().loadTexture(location, texture);
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), texture.getTextureData(), 0, bufferedImage.getWidth());
        texture.updateDynamicTexture();
        currenLoadedImageID = imageID;
        image = bufferedImage;
        hasImage = true;

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(backTexture);
        int i = (this.width - 176) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 176, 166);

        if (tempImage != null) {
            loadImage(tempImage, tempID);
            tempImage = null;
        }

        if (location != null && hasImage) {
           // GL11.glScalef(1.25F, 1.25F, 0F);
            drawTextureAt(i + 13, j + 7, location, image);
          //  GL11.glScalef(1F, 1F, 0F);
        }

        if (heldStack != null) {
            NBTTagCompound compound = ItemNBTHelper.getCompound(heldStack, "tpData", true);
            List<String> data = new ArrayList<>();
            if (compound != null) {
                int dimID = compound.getInteger("dim");
                data.add("Dim: " + dimID);
                data.add("X: " + compound.getDouble("x"));
                data.add("Y: " + compound.getDouble("y"));
                data.add("Z: " + compound.getDouble("z"));
                if(dimID != player.worldObj.provider.getDimension()){
                    this.fontRendererObj.drawString("Click to travel", i + 93, j + 115, Color.yellow.getRGB());
                } else {
                    this.fontRendererObj.drawString("Cannot Travel", i + 93, j + 115, Color.red.getRGB());
                    this.fontRendererObj.drawString("Same Dimension", i + 93, j + 125, Color.red.getRGB());
                }
            }
            int p = 0;
            for (String str : data) {
                this.fontRendererObj.drawString(str, i + 20, j + 95 + (p * 10), Color.lightGray.getRGB());
                p++;
            }
        }


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
        if (heldStack != null) {
            NBTTagCompound compound = ItemNBTHelper.getCompound(heldStack, "tpData", true);
            if (compound != null) {
                int dimID = compound.getInteger("dim");
                if(dimID == player.worldObj.provider.getDimension()){
                    return;
                }
            }
            PacketHandler.sendPacketToServer(new PacketSendTPRequest(heldStack, world, player));
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
