package me.modmuss50.technicaldimensions.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import me.modmuss50.technicaldimensions.misc.CreativeTab;
import me.modmuss50.technicaldimensions.tiles.TilePortal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import reborncore.RebornCore;

/**
 * Created by Mark on 05/08/2016.
 */
public class BlockPortal extends BlockContainer implements ITexturedBlock {

    public BlockPortal() {
        super(Material.IRON);
        setCreativeTab(CreativeTab.INSTANCE);
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePortal();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public String getTextureNameFromState(IBlockState state, EnumFacing side) {
        return "technicaldimensions:blocks/portal";
    }

    @Override
    public int amountOfStates() {
        return 1;
    }
}
