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
        if(compound.hasKey("Lx")){
            controllerPos = new BlockPos(compound.getInteger("Lx"), compound.getInteger("Ly"), compound.getInteger("Lz"));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(controllerPos != null){
            compound.setInteger("Lx", controllerPos.getX());
            compound.setInteger("Ly", controllerPos.getY());
            compound.setInteger("Lz", controllerPos.getZ());
        }

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
