package me.modmuss50.technicaldimensions.client.render;

import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestSSData;
import me.modmuss50.technicaldimensions.tiles.TilePortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import reborncore.common.packets.PacketHandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Mark on 05/08/2016.
 */
public class RenderPortal extends TileEntitySpecialRenderer<TilePortal> {


    static HashMap<String, ImageData> imageDataHashMap = new HashMap<>();

    @Override
    public void renderTileEntityAt(TilePortal te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
        if (te.imageID != null && !te.imageID.isEmpty()) {
            String imageID = te.imageID;
            ImageData data;
            if (imageDataHashMap.containsKey(imageID)) {
                data = imageDataHashMap.get(imageID);
            } else {
                data = new ImageData();
                imageDataHashMap.put(imageID, data);
            }
            if (!data.hasImage && !imageID.isEmpty()) {
                if (ScreenShotUitls.imageMap.containsKey(imageID)) {
                    if (ScreenShotUitls.bufferedImageMap.containsKey(imageID)) {
                        loadImage(ScreenShotUitls.bufferedImageMap.get(imageID), imageID, data);
                    } else {
                        try {
                            BufferedImage image = ScreenShotUitls.imageFromString(ScreenShotUitls.imageMap.get(imageID));
                            ScreenShotUitls.bufferedImageMap.put(imageID, image);
                            loadImage(image, imageID, data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    PacketHandler.sendPacketToServer(new PacketRequestSSData(imageID, Minecraft.getMinecraft().thePlayer));
                }
            }
            if (data.hasImage) {
                GlStateManager.pushMatrix();
                GL11.glTranslatef((float) x - 2F, (float) y + 4f, (float) z + 0.5F);
                GL11.glRotatef(-90F, 0.0F, 0.0F, 0.5F);

                float lastX = OpenGlHelper.lastBrightnessX;
                float lastY = OpenGlHelper.lastBrightnessY;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
                GlStateManager.disableLighting();

                Minecraft.getMinecraft().renderEngine.bindTexture(data.location);
                {
                    glBegin(GL_QUADS);
                    glTexCoord2f(0, 0);
                    glVertex2f(0, 0);
                    glTexCoord2f(1, 0);
                    glVertex2f(0, 5);
                    glTexCoord2f(1, 1);
                    glVertex2f(3, 5);
                    glTexCoord2f(0, 1);
                    glVertex2f(3, 0);
                    glEnd();
                }

                {
                    GL11.glTranslatef(0F, 5, 0);
                    GL11.glRotatef(180, 1F, 0.0F, 0F);

                    glBegin(GL_QUADS);
                    glTexCoord2f(0, 0);
                    glVertex2f(0, 0);
                    glTexCoord2f(1, 0);
                    glVertex2f(0, 5);
                    glTexCoord2f(1, 1);
                    glVertex2f(3, 5);
                    glTexCoord2f(0, 1);
                    glVertex2f(3, 0);
                    glEnd();
                }
                GlStateManager.enableLighting();
                glEnable(GL_TEXTURE_2D);

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);

                GlStateManager.popMatrix();
            }
        }
    }

    public void loadImage(BufferedImage bufferedImage, String imageID, ImageData data) {
        ResourceLocation newLoc = new ResourceLocation(imageID + "_TE");
        data.location = newLoc;
        data.texture = new DynamicTexture(bufferedImage.getWidth(), bufferedImage.getHeight());
        Minecraft.getMinecraft().getTextureManager().loadTexture(data.location, data.texture);
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), data.texture.getTextureData(), 0, bufferedImage.getWidth());
        data.texture.updateDynamicTexture();
        data.image = bufferedImage;
        data.hasImage = true;

    }

    private static class ImageData {
        public BufferedImage image;
        public DynamicTexture texture;
        public ResourceLocation location;
        public boolean hasImage = false;
    }

}
