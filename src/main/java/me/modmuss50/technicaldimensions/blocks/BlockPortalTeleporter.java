package me.modmuss50.technicaldimensions.blocks;

import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.tiles.TilePortalController;
import me.modmuss50.technicaldimensions.tiles.TileTeleporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Mark on 06/08/2016.
 */
public class BlockPortalTeleporter extends BlockContainer {

    public BlockPortalTeleporter() {
        super(Material.PORTAL);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTeleporter();
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double) ((float) pos.getX() + rand.nextFloat());
        double d1 = (double) ((float) pos.getY() + 0.8F);
        double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(!worldIn.isRemote){
            if(worldIn.getTileEntity(pos) instanceof TileTeleporter){
                TileTeleporter tileTeleporter = (TileTeleporter) worldIn.getTileEntity(pos);
                if(tileTeleporter.controllerPos != null){
                    System.out.println(tileTeleporter.controllerPos);
                    if(worldIn.getTileEntity(tileTeleporter.controllerPos) instanceof TilePortalController){
                        TilePortalController controller = (TilePortalController) worldIn.getTileEntity(tileTeleporter.controllerPos);
                        if(controller != null){
                            controller.tpEntity(entityIn);
                        }
                    }
                }
            }
        }
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }
}
