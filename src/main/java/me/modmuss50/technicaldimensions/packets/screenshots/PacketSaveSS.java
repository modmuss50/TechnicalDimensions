package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.server.ServerScreenShotUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import reborncore.common.packets.SimplePacket;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This packet sends a screenshot to the server and saves it
 */
public class PacketSaveSS extends SimplePacket {


    BufferedImage image;
    EntityPlayer player;
    String imageID;
    String imageData;

    public PacketSaveSS(BufferedImage image, EntityPlayer player, String imageID) {
        this.image = image;
        this.player = player;
        this.imageID = imageID;
    }

    public PacketSaveSS() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String string = encoder.encode(bytes);
        byteArrayOutputStream.close();
        System.out.println(string.length() + " SIZE");
        byte[] imageBytes = PacketUtill.compressString(string);
        out.writeInt(imageBytes.length);
        out.writeBytes(imageBytes);
        //writeString(string, out);
        writeString(imageID, out);

        if (out.writerIndex() > 32767) {
            System.out.println("HELP!");
        }

    }

    @Override
    public void readData(ByteBuf in) throws IOException {
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

        imageID = readString(in);

        ServerScreenShotUtils.registerScreenShot(imageID, input);
    }

    @Override
    public void execute() {

    }
}
