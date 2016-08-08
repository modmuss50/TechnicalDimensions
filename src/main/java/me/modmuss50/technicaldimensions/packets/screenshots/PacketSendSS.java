package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Mark on 04/08/2016.
 */
public class PacketSendSS implements IMessage {


    String imageID;
    String imageData;

    public PacketSendSS(String imageID, String imageData) {
        this.imageID = imageID;
        this.imageData = imageData;
    }

    public PacketSendSS() {
    }

    @Override
    public void toBytes(ByteBuf out) {
        PacketBuffer packetBuffer = new PacketBuffer(out);
        out.writeInt(imageID.length());
        packetBuffer.writeString(imageID);
        byte[] imageBytes = PacketUtill.compressString(imageData);
        out.writeInt(imageBytes.length);
        out.writeBytes(imageBytes);
        // writeString(imageData, out);

    }

    @Override
    public void fromBytes(ByteBuf in) {
        PacketBuffer buffer = new PacketBuffer(in);
        imageID = buffer.readStringFromBuffer(in.readInt());

        int size = in.readInt();
        imageData = PacketUtill.decompressByteArray(in.readBytes(size).array());
        // imageData = readString(in);
    }

    public static class SendSSHandler implements IMessageHandler<PacketSendSS, IMessage> {

        @Override
        public IMessage onMessage(PacketSendSS message, MessageContext ctx) {
            if (ScreenShotUitls.imageMap.containsKey(message.imageID)) {
                ScreenShotUitls.imageMap.replace(message.imageID, message.imageData);
            } else {
                ScreenShotUitls.imageMap.put(message.imageID, message.imageData);
            }
            try {
                BufferedImage image = ScreenShotUitls.imageFromString(ScreenShotUitls.imageMap.get(message.imageID));
                ScreenShotUitls.bufferedImageMap.put(message.imageID, image);
                GuiLinkingDevice.tempImage = image;
                GuiLinkingDevice.tempID = message.imageID;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}