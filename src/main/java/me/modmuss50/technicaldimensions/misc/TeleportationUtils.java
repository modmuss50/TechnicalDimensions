package me.modmuss50.technicaldimensions.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Created by Mark on 05/08/2016.
 */
public class TeleportationUtils {

    /**
     * A more robust way to teleport entitys
     *
     */
    public static void telportEntity(Entity entity, double x, double y, double z, float yaw, float pitch, int dimID) {
        if (entity.worldObj.isRemote) {
            return;
        }
        World originalWorld = entity.worldObj;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World newWorld = server.worldServerForDimension(dimID);
        WorldServer newWorldServer = (WorldServer) newWorld;
        WorldServer oldWorldServer = (WorldServer) originalWorld;
        boolean isNewWorld = entity.dimension != dimID;

        originalWorld.updateEntityWithOptionalForce(entity, false);
        if (entity instanceof EntityPlayerMP && isNewWorld) {
            EntityPlayerMP playerMP = (EntityPlayerMP) entity;
            playerMP.closeScreen();
            playerMP.dimension = dimID;
            playerMP.connection.sendPacket(new SPacketRespawn(playerMP.dimension, playerMP.worldObj.getDifficulty(), newWorld.getWorldType(), playerMP.interactionManager.getGameType()));
            WorldServer oldServer = (WorldServer) originalWorld;
            oldServer.getPlayerChunkMap().removePlayer(playerMP);
        }
        entity.setLocationAndAngles(x, y, z, yaw, pitch);
        if (isNewWorld) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                player.closeScreen();
                originalWorld.playerEntities.remove(player);
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) entity;
                    oldWorldServer.getPlayerChunkMap().removePlayer(playerMP);
                }
                originalWorld.updateAllPlayersSleepingFlag();
                int i = entity.chunkCoordX;
                int j = entity.chunkCoordZ;
                if ((entity.addedToChunk) && (originalWorld.getChunkProvider().getLoadedChunk(i, j) != null)) {
                    originalWorld.getChunkFromChunkCoords(i, j).removeEntity(entity);
                    originalWorld.getChunkFromChunkCoords(i, j).setModified(true);
                }
                originalWorld.loadedEntityList.remove(entity);
                originalWorld.onEntityRemoved(entity);
            }
            entity.dimension = dimID;
            newWorld.spawnEntityInWorld(entity);
            entity.setWorld(newWorld);
        }
        entity.setLocationAndAngles(x, y, z, yaw, pitch);

        newWorldServer.getChunkProvider().loadChunk((int) x >> 4, (int) z >> 4);
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (isNewWorld) {
                newWorldServer.getPlayerChunkMap().addPlayer(player);
                player.interactionManager.setWorld((WorldServer) newWorld);
                player.mcServer.getPlayerList().syncPlayerInventory(player);
                player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) newWorld);
            }
            player.connection.setPlayerLocation(x, y, z, yaw, pitch);

        }
        newWorldServer.getChunkProvider().loadChunk((int) x >> 4, (int) z >> 4);
        newWorld.updateEntityWithOptionalForce(entity, false);

    }


}
