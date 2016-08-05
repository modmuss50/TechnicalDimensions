package me.modmuss50.technicaldimensions.blocks;

import me.modmuss50.technicaldimensions.misc.CreativeTab;
import me.modmuss50.technicaldimensions.tiles.TilePortal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

/**
 * Created by Mark on 05/08/2016.
 */
public class BlockPortal extends BlockContainer {

    public BlockPortal() {
        super(Material.IRON);
        setCreativeTab(CreativeTab.INSTANCE);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePortal();
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
