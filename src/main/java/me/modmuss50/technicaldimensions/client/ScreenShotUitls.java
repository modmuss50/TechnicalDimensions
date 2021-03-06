package me.modmuss50.technicaldimensions.client;

import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketSaveSS;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ScreenShotHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class ScreenShotUitls {

    public static HashMap<String, String> imageMap = new HashMap<>();
    public static HashMap<String, BufferedImage> bufferedImageMap = new HashMap<>();

    //16:9 I may need to add support for weired aspect ratios at some point
    static int width = 150;
    static int height = 84;

    public static void takeScreenShot(String id, EntityPlayer player) {
        BufferedImage image = ScreenShotHelper.createScreenshot(width, height, Minecraft.getMinecraft().getFramebuffer());
        PacketUtill.INSTANCE.sendToServer(new PacketSaveSS(resize(image, width, height), id));
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(tmp, 0, 0, null);
        graphics2D.dispose();

        return bufferedImage;
    }


    public static BufferedImage imageFromString(String string) throws IOException {
        byte[] imageData;
        Base64.Decoder decoder = Base64.getDecoder();
        imageData = decoder.decode(string);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        return ImageIO.read(byteArrayInputStream);
    }


}
