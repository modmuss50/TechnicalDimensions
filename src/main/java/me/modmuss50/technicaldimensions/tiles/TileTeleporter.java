package me.modmuss50.technicaldimensions.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * Created by Mark on 06/08/2016.
 */
public class TileTeleporter extends TileEntity {

    public BlockPos controllerPos;


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
//        if (controllerPos == null) {
//            controllerPos = new BlockPos(-1, -1, -1);
//        }
//        compound.setInteger("x", controllerPos.getX());
//        compound.setInteger("y", controllerPos.getY());
//        compound.setInteger("z", controllerPos.getZ());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
//        BlockPos pos2 = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
//        if (pos2.getY() == -1) {
//            pos2 = null;
//        }
//        controllerPos = pos2;
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
