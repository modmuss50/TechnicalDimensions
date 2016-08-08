package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.client.ClientEventHandler;
import me.modmuss50.technicaldimensions.items.ItemLinkingDevice;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.util.ItemNBTHelper;

/**
 * Created by Mark on 05/08/2016.
 */
public class PacketRequestTakeSS implements IMessage {

    String imageID;

    public PacketRequestTakeSS(String imageID) {
        this.imageID = imageID;
    }

    public PacketRequestTakeSS() {
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


    public static class PacketRequestTakeHandler implements IMessageHandler<PacketRequestTakeSS, IMessage> {
        @Override
        public IMessage onMessage(PacketRequestTakeSS message, MessageContext ctx) {
            ClientEventHandler.needsToTakeScreenShot = true;
            ClientEventHandler.imageID = message.imageID;

            if (ItemLinkingDevice.clientStack != null) {
                NBTTagCompound compound = ItemNBTHelper.getCompound(ItemLinkingDevice.clientStack, "tpData", true);
                if (compound != null) {
                    compound.setString("imageID", message.imageID);

                    return null;
                }
            }
            return null;
        }
    }
}
