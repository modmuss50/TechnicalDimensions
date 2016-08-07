package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.server.ServerScreenShotUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import reborncore.common.packets.SimplePacket;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 04/08/2016.
 */
public class PacketSendSS extends SimplePacket {


    String imageID;
    String imageData;

    public PacketSendSS(String imageID, String imageData) {
        this.imageID = imageID;
        this.imageData = imageData;
    }

    public PacketSendSS() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        writeString(imageID, out);
        byte[] imageBytes = PacketUtill.compressString(imageData);
        out.writeInt(imageBytes.length);
        out.writeBytes(imageBytes);
        // writeString(imageData, out);

    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        imageID = readString(in);

        int size = in.readInt();
        imageData = PacketUtill.decompressByteArray(in.readBytes(size).array());
        // imageData = readString(in);
    }

    @Override
    public void execute() {
        if (ScreenShotUitls.imageMap.containsKey(imageID)) {
            ScreenShotUitls.imageMap.replace(imageID, imageData);
        } else {
            ScreenShotUitls.imageMap.put(imageID, imageData);
        }
        try {
            BufferedImage image = ScreenShotUitls.imageFromString(ScreenShotUitls.imageMap.get(imageID));
            ScreenShotUitls.bufferedImageMap.put(imageID, image);
            GuiLinkingDevice.tempImage = image;
            GuiLinkingDevice.tempID = imageID;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}