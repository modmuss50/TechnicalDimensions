package me.modmuss50.technicaldimensions.packets;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
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

    public PacketSaveSS(BufferedImage image, EntityPlayer player) {
        this.image = image;
        this.player = player;
    }

    public PacketSaveSS() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        PacketBuffer packetBuffer = new PacketBuffer(out);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String string = encoder.encode(bytes);
        byteArrayOutputStream.close();

        //System.out.println(string);
        System.out.println(string.length() + " SIZE");
        writeString(string, out);

        ScreenShotUitls.clientSideImage = image;
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        String input = readString(in);
        byte[] imageData;
        BASE64Decoder decoder = new BASE64Decoder();
        imageData = decoder.decodeBuffer(input);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        image = ImageIO.read(byteArrayInputStream);
        byteArrayInputStream.close();

        System.out.println(image);

        ServerScreenShotUtils.image = image;
    }

    @Override
    public void execute() {

    }
}
