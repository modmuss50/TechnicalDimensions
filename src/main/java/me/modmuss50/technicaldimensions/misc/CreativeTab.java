package me.modmuss50.technicaldimensions.misc;

import me.modmuss50.technicaldimensions.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Mark on 04/08/2016.
 */
public class CreativeTab extends CreativeTabs {

    public static final CreativeTabs INSTANCE = new CreativeTab();

    public CreativeTab() {
        super("TechnicalDimensions");
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.linkingDevice;
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ModItems.linkingDevice, 1, 1);
    }
}
