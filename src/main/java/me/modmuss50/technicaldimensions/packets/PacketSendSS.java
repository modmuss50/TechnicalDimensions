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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Mark on 04/08/2016.
 */
public class PacketSendSS extends SimplePacket {


    EntityPlayer player;

    public PacketSendSS(EntityPlayer player) {
        this.player = player;
    }

    public PacketSendSS() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        PacketBuffer packetBuffer = new PacketBuffer(out);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(ServerScreenShotUtils.image, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String string = encoder.encode(bytes);
        byteArrayOutputStream.close();

        //System.out.println(string);
        System.out.println(string.length() + " SIZE");
        writeString(string, out);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        String input = readString(in);
        byte[] imageData;
        BASE64Decoder decoder = new BASE64Decoder();
        imageData = decoder.decodeBuffer(input);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        ScreenShotUitls.clientSideImage = ImageIO.read(byteArrayInputStream);
        byteArrayInputStream.close();

        System.out.println(ScreenShotUitls.clientSideImage);

    }

    @Override
    public void execute() {

    }
}