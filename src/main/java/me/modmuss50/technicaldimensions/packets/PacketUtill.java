package me.modmuss50.technicaldimensions.packets;

import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestSSData;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestTakeSS;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketSaveSS;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketSendSS;
import me.modmuss50.technicaldimensions.packets.teleportation.PacketSendTPRequest;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class PacketUtill {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("technicaldimensions");

    private static int id = 0;

    public static int getNextID() {
        return id++;
    }

    public static void init() {
        INSTANCE.registerMessage(PacketSendTPRequest.TPRequestHandler.class, PacketSendTPRequest.class, getNextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketSaveSS.SaveSSHandler.class, PacketSaveSS.class, getNextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketRequestSSData.RequestSSDataHandler.class, PacketRequestSSData.class, getNextID(), Side.SERVER);

        INSTANCE.registerMessage(PacketSendSS.SendSSHandler.class, PacketSendSS.class, getNextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketRequestTakeSS.PacketRequestTakeHandler.class, PacketRequestTakeSS.class, getNextID(), Side.CLIENT);

    }

    /**
     * Thanks Jared
     */
    public static byte[] compressString(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream;
        try {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(str.getBytes("UTF-8"));
            gzipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }


    public static String decompressByteArray(byte[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(array));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
