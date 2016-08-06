package me.modmuss50.technicaldimensions.tiles;

import me.modmuss50.technicaldimensions.misc.TeleportationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

/**
 * Created by Mark on 05/08/2016.
 */
public class TilePortalController extends TileEntity {

    public String imageID;

    public double x, y, z;
    public float yaw, pitch;
    public int dim;

    public void tpEntity(Entity entity){
        if(imageID != null && !imageID.isEmpty())
            TeleportationUtils.teleportEntity(entity, x, y, z, yaw, pitch, dim);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        imageID = compound.getString("imageID");
        if (imageID.equals("_")) {
            imageID = "";
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("imageID", imageID == null || imageID.isEmpty() ? "_" : imageID);
        return compound;
    }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.writeToNBT(new NBTTagCompound());
        writeToNBT(compound);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }
}