package me.modmuss50.technicaldimensions.client;

import me.modmuss50.technicaldimensions.misc.LinkingIDHelper;
import me.modmuss50.technicaldimensions.packets.PacketSaveSS;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ScreenShotHelper;
import reborncore.common.packets.PacketHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ScreenShotUitls {


    public static BufferedImage clientSideImage;

    public static void takeScreenShot(ItemStack stack, EntityPlayer player) {
        BufferedImage image = ScreenShotHelper.createScreenshot(150, 100, Minecraft.getMinecraft().getFramebuffer());
        Optional<String> stringOptional = LinkingIDHelper.getIDFromStack(stack);
        if(!stringOptional.isPresent()){
            return;
        }
        PacketHandler.sendPacketToServer(new PacketSaveSS(resize(image, 150, 100), player, LinkingIDHelper.getIDFromStack(stack).get()));
    }

    public static BufferedImage resize(BufferedImage image, int width, int height){
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(tmp, 0, 0, null);
        graphics2D.dispose();

        return bufferedImage;
    }


}
