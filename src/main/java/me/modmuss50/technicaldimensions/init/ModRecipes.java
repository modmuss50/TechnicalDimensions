package me.modmuss50.technicaldimensions.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.CraftingHelper;

/**
 * Created by Mark on 08/08/2016.
 */
public class ModRecipes {

    public static void load() {
        CraftingHelper
                .addShapedOreRecipe(new ItemStack(ModItems.linkingDevice),
                        "III",
                        "IEI",
                        "IRI",
                        'I', "ingotIron",
                        'E', new ItemStack(Items.ENDER_PEARL),
                        'R', "dustRedstone");

        CraftingHelper
                .addShapedOreRecipe(new ItemStack(ModItems.linkingDevice, 1, 2),
                        "III",
                        "IDI",
                        "IEI",
                        'I', "ingotIron",
                        'D', "blockDiamond",
                        'E', new ItemStack(Items.ENDER_EYE));

        CraftingHelper
                .addShapedOreRecipe(new ItemStack(ModBlocks.portalController),
                        "III",
                        "ILI",
                        "III",
                        'I', "blockIron",
                        'L', new ItemStack(ModItems.linkingDevice));
    }
}
