package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.server.ServerScreenShotUtils;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.packets.PacketHandler;
import reborncore.common.packets.SimplePacket;

import java.io.IOException;

public class PacketRequestSSData extends SimplePacket {

    String imageID;
    EntityPlayer player;

    public PacketRequestSSData(String imageID, EntityPlayer player) {
        this.imageID = imageID;
        this.player = player;
    }

    public PacketRequestSSData() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        writeString(imageID, out);
        writePlayer(player, out);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        imageID = readString(in);
        player = readPlayer(in);
    }

    @Override
    public void execute() {
        String image = ServerScreenShotUtils.getScreenshotData(imageID);
        if (image == null) {
            //Image not found
            return;
        }
        PacketHandler.sendPacketToPlayer(new PacketSendSS(imageID, ServerScreenShotUtils.getScreenshotData(imageID)), player);
    }
}
