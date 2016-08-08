package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.server.ServerScreenShotUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This packet sends a screenshot to the server and saves it
 */
public class PacketSaveSS implements IMessage {


    BufferedImage image;
    String imageID;
    String imageData;

    public PacketSaveSS(BufferedImage image, String imageID) {
        this.image = image;
        this.imageID = imageID;
    }

    public PacketSaveSS() {
    }

    @Override
    public void toBytes(ByteBuf out) {
        PacketBuffer buffer = new PacketBuffer(out);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            String string = encoder.encode(bytes);
            byteArrayOutputStream.close();
            byte[] imageBytes = PacketUtill.compressString(string);
            out.writeInt(imageBytes.length);
            out.writeBytes(imageBytes);
            out.writeInt(imageID.length());
            buffer.writeString(imageID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromBytes(ByteBuf in) {
        PacketBuffer buffer = new PacketBuffer(in);
        try {
            int size = in.readInt();
            String input = PacketUtill.decompressByteArray(in.readBytes(size).array());
            imageData = input;

            //TODO check if needed
            byte[] imageData;
            BASE64Decoder decoder = new BASE64Decoder();
            imageData = decoder.decodeBuffer(input);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
            image = ImageIO.read(byteArrayInputStream);
            byteArrayInputStream.close();

            imageID = buffer.readStringFromBuffer(in.readInt());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class SaveSSHandler implements IMessageHandler<PacketSaveSS, IMessage> {

        @Override
        public IMessage onMessage(PacketSaveSS message, MessageContext ctx) {
            ServerScreenShotUtils.registerScreenShot(message.imageID, message.imageData);
            return null;
        }
    }
}
