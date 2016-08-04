package me.modmuss50.technicaldimensions.packets;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.misc.CustomTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import reborncore.common.packets.SimplePacket;
import reborncore.common.util.ItemNBTHelper;

import java.io.IOException;

/**
 * Created by Mark on 04/08/2016.
 */
public class PacketSendTPRequest extends SimplePacket {

    ItemStack stack;

    NBTTagCompound compound;

    double x, y, z;
    float yaw, pitch;
    int dim;

    World world;
    EntityPlayer player;

    public PacketSendTPRequest(ItemStack stack, World world, EntityPlayer player) {
        this.stack = stack;
        this.world = world;
        this.player = player;
    }

    public PacketSendTPRequest() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        compound = ItemNBTHelper.getCompound(stack, "tpData", true);
        if (compound != null) {
            out.writeDouble(compound.getDouble("x"));
            out.writeDouble(compound.getDouble("y"));
            out.writeDouble(compound.getDouble("z"));
            out.writeFloat(compound.getFloat("yaw"));
            out.writeFloat(compound.getFloat("pitch"));
            out.writeInt(compound.getInteger("dim"));
            writeWorld(world, out);
            writePlayer(player, out);
        }
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
        dim = in.readInt();
        world = readWorld(in);
        player = readPlayer(in);
    }

    @Override
    public void execute() {
        CustomTeleporter teleporter = new CustomTeleporter((WorldServer) world, x, y, z, yaw, pitch);
        PlayerList playerList = world.getMinecraftServer().getPlayerList();
        playerList.transferPlayerToDimension((EntityPlayerMP) player, dim, teleporter);
    }
}
