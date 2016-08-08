package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.server.ServerScreenShotUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestSSData implements IMessage {

    String imageID;

    public PacketRequestSSData(String imageID) {
        this.imageID = imageID;
    }

    public PacketRequestSSData() {
    }

    @Override
    public void toBytes(ByteBuf out) {
        PacketBuffer buffer = new PacketBuffer(out);
        out.writeInt(imageID.length());
        buffer.writeString(imageID);
    }

    @Override
    public void fromBytes(ByteBuf in) {
        PacketBuffer buffer = new PacketBuffer(in);
        imageID = buffer.readStringFromBuffer(in.readInt());
    }


    public static class RequestSSDataHandler implements IMessageHandler<PacketRequestSSData, IMessage> {
        @Override
        public IMessage onMessage(PacketRequestSSData message, MessageContext ctx) {
            String image = ServerScreenShotUtils.getScreenshotData(message.imageID);
            if (image == null) {
                //Image not found
                return null;
            }
            PacketUtill.INSTANCE.sendTo(new PacketSendSS(message.imageID, ServerScreenShotUtils.getScreenshotData(message.imageID)), ctx.getServerHandler().playerEntity);
            return null;
        }
    }

}
