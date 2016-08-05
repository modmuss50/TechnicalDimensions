package me.modmuss50.technicaldimensions.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Mark on 05/08/2016.
 */
public class ClientEventHandler {

    public static boolean needsToTakeScreenShot = false;
    public static String imageID;
    public static EntityPlayer player;

    @SubscribeEvent
    public static void renderTick(RenderWorldLastEvent event) {
        if (needsToTakeScreenShot) {
            ScreenShotUitls.takeScreenShot(imageID, Minecraft.getMinecraft().thePlayer);
            needsToTakeScreenShot = false;
        }
    }


}
