package me.modmuss50.technicaldimensions.client.render;

import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestSSData;
import me.modmuss50.technicaldimensions.tiles.TilePortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
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

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Mark on 05/08/2016.
 */
public class RenderPortal extends TileEntitySpecialRenderer<TilePortal> {

    public static BufferedImage image;
    static DynamicTexture texture;
    static ResourceLocation location;
    public static String currenLoadedImageID;
    public static String neededImageID;
    private static boolean hasImage = false;

    public static BufferedImage tempImage = null;
    public static String tempID = null;

    public boolean hasLoadedImage = false;

    private static ModelPortal model = new ModelPortal();

    @Override
    public void renderTileEntityAt(TilePortal te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
        if(te.imageID != null && !te.imageID.isEmpty()){
            String imageID = te.imageID;
            if (!hasLoadedImage && !imageID.isEmpty()) {
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
                    PacketHandler.sendPacketToServer(new PacketRequestSSData(imageID, Minecraft.getMinecraft().thePlayer));
                }
            }
            if(hasImage){
                GlStateManager.pushMatrix();

//                GlStateManager.depthMask(false);
//                GlStateManager.enableBlend();
//                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
//
                GL11.glTranslatef((float) x + 1F, (float) y + 4f, (float) z + 0.5F);
                GL11.glRotatef(-90F, 0.0F, 0.0F, 0.5F);
                Minecraft.getMinecraft().renderEngine.bindTexture(location);

               //model.Shape1.render(0.0625F);

                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer buffer = tessellator.getBuffer();
               // buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
//                drawQuad();
        //        glDisable(GL_TEXTURE_2D);
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

                glEnable(GL_TEXTURE_2D);
             //   tessellator.draw();

                GlStateManager.popMatrix();
            }
        }
    }

    public void drawQuad() {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.pos(-0.5D, 0.5D, 0.0D).tex(0.0D, 1.0D).endVertex();
        buffer.pos(0.5D, 0.5D, 0.0D).tex(1.0D, 1.0D).endVertex();
        buffer.pos(0.5D, -0.5D, 0.0D).tex(1.0D, 0.0D).endVertex();
        buffer.pos(-0.5D, -0.5D, 0.0D).tex(0.0D, 0.0D).endVertex();
    }



    public void loadImage(BufferedImage bufferedImage, String imageID) {
        ResourceLocation newLoc = new ResourceLocation(imageID + "_TE");
        location = newLoc;
        texture = new DynamicTexture(bufferedImage.getWidth(), bufferedImage.getHeight());
        Minecraft.getMinecraft().getTextureManager().loadTexture(location, texture);
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), texture.getTextureData(), 0, bufferedImage.getWidth());
        texture.updateDynamicTexture();
        currenLoadedImageID = imageID;
        image = bufferedImage;
        hasImage = true;
        hasLoadedImage = true;

    }

    private static class ModelPortal extends ModelBase {

        public ModelRenderer Shape1;

        public ModelPortal()
        {
            textureWidth = 300;
            textureHeight = 84;

            Shape1 = new ModelRenderer(this, 0, 0);
            Shape1.addBox(0F, 0F, 0F, 80, 48, 1);
            Shape1.setRotationPoint(0F, 0F, 0F);
            Shape1.setTextureSize(150, 84);
            Shape1.mirror = true;
            setRotation(Shape1, 0F, 0F, 0F);
        }

        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
        {
            super.render(entity, f, f1, f2, f3, f4, f5);
            setRotationAngles(f, f1, f2, f3, f4, f5, entity);
            Shape1.render(f5);
        }

        private void setRotation(ModelRenderer model, float x, float y, float z)
        {
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }

        public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
        {
            super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        }
    }
}
