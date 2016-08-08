package me.modmuss50.technicaldimensions.tiles;

import me.modmuss50.technicaldimensions.blocks.BlockPortalController;
import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.misc.TeleportationUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Mark on 05/08/2016.
 */
public class TilePortalController extends TileEntity implements ITickable {

    public String imageID;

    public double x, y, z;
    public float yaw, pitch;
    public int dim;

    public boolean rotated = false;

    public void tpEntity(Entity entity) {
        if (imageID != null && !imageID.isEmpty())
            TeleportationUtils.teleportEntity(entity, x, y, z, yaw, pitch, dim);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("imageID")) {
            imageID = compound.getString("imageID");
        }

        x = compound.getDouble("Lx");
        y = compound.getDouble("Ly");
        z = compound.getDouble("Lz");
        yaw = compound.getFloat("yaw");
        pitch = compound.getFloat("pitch");
        dim = compound.getInteger("dimID");

        rotated = compound.getBoolean("rotated");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (imageID != null && !imageID.isEmpty()) {
            compound.setString("imageID", imageID);
        }

        compound.setDouble("Lx", x);
        compound.setDouble("Ly", y);
        compound.setDouble("Lz", z);
        compound.setFloat("yaw", yaw);
        compound.setFloat("pitch", pitch);
        compound.setInteger("dimID", dim);

        compound.setBoolean("rotated", rotated);

        return compound;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
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

    @Override
    public void update() {
        if (worldObj.getTotalWorldTime() % 20 == 0) {
            if (worldObj.getBlockState(getPos()).getBlock() == ModBlocks.portalController) {
                rotated = worldObj.getBlockState(getPos()).getValue(BlockPortalController.ROTATED);
            }

        }
    }
}
