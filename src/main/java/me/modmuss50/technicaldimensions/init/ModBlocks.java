package me.modmuss50.technicaldimensions.init;

import me.modmuss50.technicaldimensions.blocks.BlockPortalController;
import me.modmuss50.technicaldimensions.blocks.BlockPortalTeleporter;
import me.modmuss50.technicaldimensions.tiles.TilePortalController;
import me.modmuss50.technicaldimensions.tiles.TileTeleporter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Mark on 05/08/2016.
 */
public class ModBlocks {

    public static BlockPortalController portalController;
    public static Block teleporter;

    public static void load() {
        portalController = new BlockPortalController();
        registerBlock(portalController, "portalController");
        GameRegistry.registerTileEntity(TilePortalController.class, "technicaldimensions.portalController");

        teleporter = new BlockPortalTeleporter();
        registerBlock(teleporter, "teleporter");
        GameRegistry.registerTileEntity(TileTeleporter.class, "technicaldimensions.teleporter");
    }


    public static void registerBlock(Block block, String name) {
        block.setRegistryName(new ResourceLocation("technicaldimensions", name));
        GameRegistry.register(block);
        block.setUnlocalizedName(block.getRegistryName().toString());
        GameRegistry.register(new ItemBlock(block), block.getRegistryName());
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
        block.setRegistryName(new ResourceLocation("technicaldimensions", name));
        block.setUnlocalizedName(block.getRegistryName().toString());
        GameRegistry.register(block);
        try {
            ItemBlock itemBlock = itemclass.getConstructor(Block.class).newInstance(block);
            itemBlock.setRegistryName(name);
            GameRegistry.register(itemBlock);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
