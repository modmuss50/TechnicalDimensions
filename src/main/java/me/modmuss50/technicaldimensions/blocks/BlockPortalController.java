package me.modmuss50.technicaldimensions.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.misc.CreativeTab;
import me.modmuss50.technicaldimensions.tiles.TilePortalController;
import me.modmuss50.technicaldimensions.tiles.TileTeleporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.RebornCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 05/08/2016.
 */
public class BlockPortalController extends BlockContainer implements ITexturedBlock {

    public BlockPortalController() {
        super(Material.IRON);
        setCreativeTab(CreativeTab.INSTANCE);
        RebornCore.jsonDestroyer.registerObject(this);
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
        return "technicaldimensions:blocks/portalController";
    }

    @Override
    public int amountOfStates() {
        return 1;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            List<BlockPos> poses = getTeleporterBlockLocations(new BlockPos(pos));
            for (BlockPos blockPos : poses) {
                System.out.println(pos);
                worldIn.setBlockState(blockPos, ModBlocks.teleporter.getDefaultState());
                TileTeleporter tileTeleporter = (TileTeleporter) worldIn.getTileEntity(blockPos);
                tileTeleporter.controllerPos = new BlockPos(pos);
                System.out.println(pos);
            }
        }

    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        List<BlockPos> poses = getTeleporterBlockLocations(pos);
        for (BlockPos blockPos : poses) {
            worldIn.setBlockToAir(blockPos);
        }
    }

    public List<BlockPos> getTeleporterBlockLocations(BlockPos pos) {
        List<BlockPos> poses = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = -2; x < 3; x++) {
                poses.add(pos.add(x, y + 1, 0));
            }
        }
        return poses;
    }
}
