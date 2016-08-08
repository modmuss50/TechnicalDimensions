package me.modmuss50.technicaldimensions.packets.teleportation;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.misc.TeleportationUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.util.ItemNBTHelper;

/**
 * Created by Mark on 04/08/2016.
 */
public class PacketSendTPRequest implements IMessage {

    ItemStack stack;

    NBTTagCompound compound;

    double x, y, z;
    float yaw, pitch;
    int dim;


    public PacketSendTPRequest(ItemStack stack) {
        this.stack = stack;
    }

    public PacketSendTPRequest() {
    }

    @Override
    public void toBytes(ByteBuf out) {
        compound = ItemNBTHelper.getCompound(stack, "tpData", true);
        if (compound != null) {
            out.writeDouble(compound.getDouble("x"));
            out.writeDouble(compound.getDouble("y"));
            out.writeDouble(compound.getDouble("z"));
            out.writeFloat(compound.getFloat("yaw"));
            out.writeFloat(compound.getFloat("pitch"));
            out.writeInt(compound.getInteger("dim"));
        }
    }

    @Override
    public void fromBytes(ByteBuf in) {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
        dim = in.readInt();
    }

//    @Override
//    public void execute() {
////        CustomTeleporter teleporter = new CustomTeleporter((WorldServer) world, x, y, z, yaw, pitch);
////        PlayerList playerList = world.getMinecraftServer().getPlayerList();
////        int playerDimID = player.worldObj.provider.getDimension();
////        if(playerDimID == dim){
////            //TODO fix this
////            player.setLocationAndAngles(x, y, z, yaw, pitch);
////        } else {
////            playerList.transferPlayerToDimension((EntityPlayerMP) player, dim, teleporter);
////        }
//
//
//    }

    public static class TPRequestHandler implements IMessageHandler<PacketSendTPRequest, IMessage> {

        @Override
        public IMessage onMessage(PacketSendTPRequest message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendTPRequest message, MessageContext ctx) {
            TeleportationUtils.teleportEntity(ctx.getServerHandler().playerEntity, message.x, message.y, message.z, message.yaw, message.pitch, message.dim);
        }
    }

}
