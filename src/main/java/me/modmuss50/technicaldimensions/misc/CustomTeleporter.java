package me.modmuss50.technicaldimensions.misc;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * Created by Mark on 04/08/2016.
 */
public class CustomTeleporter extends Teleporter {

    double x, y, z;
    float yaw, pitch;

    public CustomTeleporter(WorldServer worldIn, double x, double y, double z, float yaw, float pitch) {
        super(worldIn);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        entityIn.setLocationAndAngles(x, y, z, yaw, pitch);
    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        return true;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        //TODO make spawn pad
        return true;
    }

    @Override
    public void removeStalePortalLocations(long worldTime) {

    }
}
