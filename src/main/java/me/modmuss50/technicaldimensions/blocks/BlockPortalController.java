package me.modmuss50.technicaldimensions.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.misc.CreativeTab;
import me.modmuss50.technicaldimensions.tiles.TilePortalController;
import me.modmuss50.technicaldimensions.tiles.TileTeleporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import reborncore.RebornCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 05/08/2016.
 */
public class BlockPortalController extends BlockContainer implements ITexturedBlock {

    public static PropertyBool ROTATED = PropertyBool.create("rotated");

    public BlockPortalController() {
        super(Material.IRON);
        setCreativeTab(CreativeTab.INSTANCE);
        RebornCore.jsonDestroyer.registerObject(this);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(ROTATED, false));
    }

    protected BlockStateContainer createBlockState() {
        ROTATED = PropertyBool.create("rotated");
        return new BlockStateContainer(this, ROTATED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ROTATED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ROTATED, meta == 1);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePortalController();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public String getTextureNameFromState(IBlockState state, EnumFacing side) {
        if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
            return "technicaldimensions:blocks/portal";
        }
        return "technicaldimensions:blocks/portal_controller";
    }

    @Override
    public int amountOfStates() {
        return 2;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        Boolean rotated = false;
        if (enumfacing == EnumFacing.WEST || enumfacing == EnumFacing.EAST) {
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(ROTATED, true));
            rotated = true;
        }

        if (!worldIn.isRemote) {
            List<BlockPos> poses = getTeleporterBlockLocations(new BlockPos(pos), worldIn, rotated);
            for (BlockPos blockPos : poses) {
                worldIn.setBlockState(blockPos, ModBlocks.teleporter.getDefaultState());
                TileTeleporter tileTeleporter = (TileTeleporter) worldIn.getTileEntity(blockPos);
                tileTeleporter.controllerPos = new BlockPos(pos);
            }
        }

    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        List<BlockPos> poses = getTeleporterBlockLocations(pos, worldIn, false);
        for (BlockPos blockPos : poses) {
            if (worldIn.getBlockState(blockPos).getBlock() == ModBlocks.teleporter) {
                worldIn.setBlockToAir(blockPos);
            }
        }
        poses = getTeleporterBlockLocations(pos, worldIn, true);
        for (BlockPos blockPos : poses) {
            if (worldIn.getBlockState(blockPos).getBlock() == ModBlocks.teleporter) {
                worldIn.setBlockToAir(blockPos);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    public List<BlockPos> getTeleporterBlockLocations(BlockPos pos, World world, boolean rotated) {
        List<BlockPos> poses = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = -2; x < 3; x++) {
                BlockPos newPos = pos.add(rotated ? 0 : x, y + 1, rotated ? x : 0);
                if (world.getBlockState(newPos).getBlock() == Blocks.AIR || world.getBlockState(newPos).getBlock() == ModBlocks.teleporter) {
                    poses.add(newPos);
                }

            }
        }
        return poses;
    }
}
