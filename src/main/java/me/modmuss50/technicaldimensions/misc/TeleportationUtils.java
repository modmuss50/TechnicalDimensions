package me.modmuss50.technicaldimensions.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 05/08/2016.
 */
public class TeleportationUtils {

    static List<TPData> queuedTps = new ArrayList<>();

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (!queuedTps.isEmpty()) {
            for (TPData data : queuedTps) {
                if (data.entity instanceof EntityPlayerMP) {
                    data.entity.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP) data.entity, data.dim, new CTeleporter((WorldServer) data.entity.worldObj, data.getX(), data.getY(), data.getZ(), data.getYaw(), data.getPitch()));
                } else {
                    //TODO move entitys
                    if (data.dim != data.entity.worldObj.provider.getDimension()) {
                        data.entity.setLocationAndAngles(data.x, data.y, data.z, data.yaw, data.pitch);
                    }
                }
            }
            queuedTps.clear();
        }
    }

    public static class CTeleporter extends Teleporter {

        WorldServer server;

        double x, y, z;
        float yaw, pitch;

        public CTeleporter(WorldServer worldIn, double x, double y, double z, float yaw, float pitch) {
            super(worldIn);
            this.server = worldIn;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        @Override
        public void placeInPortal(Entity entityIn, float rotationYaw) {
            server.getBlockState(new BlockPos(x, y, z));

            entityIn.setLocationAndAngles(x, y, z, yaw, pitch);
            entityIn.motionX = 0.0D;
            entityIn.motionY = 0.0D;
            entityIn.motionZ = 0.0D;
        }
    }

    public static class TPData {

        double x, y, z;
        float yaw, pitch;
        int dim;

        Entity entity;

        public TPData(double x, double y, double z, float yaw, float pitch, int dim, Entity entity) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.dim = dim;
            this.entity = entity;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public int getDim() {
            return dim;
        }

        public Entity getEntity() {
            return entity;
        }
    }


    public static void teleportEntity(Entity entity, BlockPos pos, int dimID) {
        teleportEntity(entity, pos.getX() + 0.5, pos.getY() + 1, pos.getZ(), 0, 0, dimID);
    }

    public static void teleportEntity(Entity entity, double x, double y, double z, float yaw, float pitch, int dimID) {
        teleportEntity(entity, x, y, z, yaw, pitch, dimID, false);
    }

    /**
     * A more robust way to teleport entitys
     */
    public static void teleportEntity(Entity entity, double x, double y, double z, float yaw, float pitch, int dimID, boolean findSpawn) {
        if (entity == null || entity.worldObj == null || entity.worldObj.isRemote) {
            return;
        }
        if (!DimensionManager.isDimensionRegistered(dimID)) {
            return;
        }
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World newWorld = server.worldServerForDimension(dimID);
        if (findSpawn) {
            BlockPos spawnCoordinate = newWorld.getSpawnPoint();
            x = (double) spawnCoordinate.getX() + 0.5;
            y = (double) spawnCoordinate.getY() + 0.5;
            z = (double) spawnCoordinate.getZ() + 0.5;
        }
        TPData data = new TPData(x, y, z, yaw, pitch, dimID, entity);
        queuedTps.add(data);

//
//        World originalWorld = entity.worldObj;
//        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
//        World newWorld = server.worldServerForDimension(dimID);
//        WorldServer newWorldServer = (WorldServer) newWorld;
//        WorldServer oldWorldServer = (WorldServer) originalWorld;
//        boolean isNewWorld = entity.dimension != dimID;
//

//
//        if (dimID != 0 && isNewWorld) {
//            for (int i = -3; i < 3; i++) {
//                for (int j = -3; j < 3; j++) {
//                    newWorld.setBlockState(spawnCoordinate.add(i, -1, j), Blocks.STONE.getDefaultState());
//                }
//            }
//        }
//
//        originalWorld.updateEntityWithOptionalForce(entity, false);
//        if (entity instanceof EntityPlayerMP && isNewWorld) {
//            EntityPlayerMP playerMP = (EntityPlayerMP) entity;
//            playerMP.closeScreen();
//            playerMP.dimension = dimID;
//            playerMP.connection.sendPacket(new SPacketRespawn(playerMP.dimension, playerMP.worldObj.getDifficulty(), newWorld.getWorldType(), playerMP.interactionManager.getGameType()));
//            WorldServer oldServer = (WorldServer) originalWorld;
//            oldServer.getPlayerChunkMap().removePlayer(playerMP);
//        }
//        entity.setLocationAndAngles(x, y, z, yaw, pitch);
//        if (isNewWorld) {
//            if (entity instanceof EntityPlayer) {
//                EntityPlayer player = (EntityPlayer) entity;
//                player.closeScreen();
//                originalWorld.playerEntities.remove(player);
//                originalWorld.updateAllPlayersSleepingFlag();
//                int i = entity.chunkCoordX;
//                int j = entity.chunkCoordZ;
//                if ((entity.addedToChunk) && (originalWorld.getChunkProvider().getLoadedChunk(i, j) != null)) {
//                    originalWorld.getChunkFromChunkCoords(i, j).removeEntity(entity);
//                    originalWorld.getChunkFromChunkCoords(i, j).setModified(true);
//                }
//                originalWorld.loadedEntityList.remove(entity);
//                originalWorld.onEntityRemoved(entity);
//            }
//            entity.dimension = dimID;
//            newWorld.spawnEntityInWorld(entity);
//            entity.setWorld(newWorld);
//        }
//        entity.setLocationAndAngles(x, y, z, yaw, pitch);
//
//        newWorldServer.getChunkProvider().loadChunk((int) x >> 4, (int) z >> 4);
//        if (entity instanceof EntityPlayerMP) {
//            EntityPlayerMP player = (EntityPlayerMP) entity;
//            if (isNewWorld) {
//                //newWorldServer.getPlayerChunkMap().addPlayer(player);
//                player.interactionManager.setWorld((WorldServer) newWorld);
//                player.mcServer.getPlayerList().syncPlayerInventory(player);
//                player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) newWorld);
//            }
//            player.connection.setPlayerLocation(x, y, z, yaw, pitch);
//
//        }
//        newWorldServer.getChunkProvider().loadChunk((int) x >> 4, (int) z >> 4);
//        newWorld.updateEntityWithOptionalForce(entity, false);
//        entity.setLocationAndAngles(x, y, z, yaw, pitch);
//
//        entity.worldObj.getBlockState(new BlockPos(x, y, z));

    }

}
