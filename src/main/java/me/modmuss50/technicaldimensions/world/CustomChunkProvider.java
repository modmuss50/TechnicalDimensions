package me.modmuss50.technicaldimensions.world;

import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderOverworld;

/**
 * Created by Mark on 05/08/2016.
 */
public class CustomChunkProvider extends ChunkProviderOverworld {
    public CustomChunkProvider(World worldIn, long seed, boolean mapFeaturesEnabledIn, String settings) {
        super(worldIn, seed, mapFeaturesEnabledIn, settings);
    }


}
