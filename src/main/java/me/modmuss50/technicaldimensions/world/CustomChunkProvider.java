package me.modmuss50.technicaldimensions.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderOverworld;

/**
 * Created by Mark on 05/08/2016.
 */
public class CustomChunkProvider {

    public static IChunkGenerator getGenForWorld(DimData data, World worldIn, long seed, boolean mapFeaturesEnabledIn, String settings) {
        if (data.falt) {
            return new ChunkProviderFlat(worldIn, seed, mapFeaturesEnabledIn, "3;minecraft:bedrock,60*minecraft:stone,2*minecraft:dirt,minecraft:grass;3;village(size=50 distance=50),mineshaft(chance=0.5),biome_1,dungeon,decoration,lake,lava_lake");
        }
        //TODO get custom shit
        return new ChunkProviderOverworld(worldIn, seed, mapFeaturesEnabledIn, settings);
    }

}
