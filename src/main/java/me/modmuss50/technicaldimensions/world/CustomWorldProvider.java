package me.modmuss50.technicaldimensions.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

/**
 * Created by Mark on 05/08/2016.
 */
public class CustomWorldProvider extends WorldProvider {

    @Override
    public IChunkGenerator createChunkGenerator() {
        //Random world each time
        return new CustomChunkProvider(worldObj, worldObj.getSeed() + getDimension(), true, "");
    }

    @Override
    public DimensionType getDimensionType() {
        return ModDimensions.getDimTypeFromID(getDimension());
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return worldObj.getTopSolidOrLiquidBlock(new BlockPos(0, 0, 0));
    }

    @Override
    public BlockPos getRandomizedSpawnPoint() {
        return worldObj.getTopSolidOrLiquidBlock(new BlockPos(0, 0, 0));
    }

    @Override
    public BlockPos getSpawnPoint() {
        return worldObj.getTopSolidOrLiquidBlock(new BlockPos(0, 0, 0));
    }
}
