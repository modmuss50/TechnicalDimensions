package me.modmuss50.technicaldimensions.packets.screenshots;

import io.netty.buffer.ByteBuf;
import me.modmuss50.technicaldimensions.client.ClientEventHandler;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.items.ItemLinkingDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import reborncore.common.packets.SimplePacket;
import reborncore.common.util.ItemNBTHelper;

import java.io.IOException;

/**
 * Created by Mark on 05/08/2016.
 */
public class PacketRequestTakeSS extends SimplePacket {

    String imageID;
    EntityPlayer player;

    public PacketRequestTakeSS(String imageID, EntityPlayer player) {
        this.imageID = imageID;
        this.player = player;
    }

    public PacketRequestTakeSS() {
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
        System.out.println("Requesting client to take " + imageID);
        //TODO take from render thread
        ClientEventHandler.needsToTakeScreenShot = true;
        ClientEventHandler.imageID = imageID;
        ClientEventHandler.player = player;

        if(ItemLinkingDevice.clientStack != null){
            NBTTagCompound compound = ItemNBTHelper.getCompound(ItemLinkingDevice.clientStack, "tpData", true);
            if(compound != null){
                compound.setString("imageID", imageID);
                System.out.println("Setting " + imageID);
            }
        }
    }
}
