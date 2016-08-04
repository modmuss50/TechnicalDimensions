package me.modmuss50.technicaldimensions.init;

import me.modmuss50.technicaldimensions.items.ItemLinkingDevice;
import me.modmuss50.technicaldimensions.items.ItemUnlinkedDevice;
import me.modmuss50.technicaldimensions.misc.CreativeTab;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.RebornCore;

public class ModItems {

    public static Item unlinkedDevice;
    public static Item linkingDevice;

    public static void load() {
        unlinkedDevice = new ItemUnlinkedDevice();
        registerItem(unlinkedDevice, "unlinkedDevice");

        linkingDevice = new ItemLinkingDevice();
        registerItem(linkingDevice, "linkingDevice");
    }

    private static void registerItem(Item item, String name) {
        item.setRegistryName(new ResourceLocation("technicaldimensions", name));
        item.setUnlocalizedName(item.getRegistryName().toString());
        item.setCreativeTab(CreativeTab.INSTANCE);
        RebornCore.jsonDestroyer.registerObject(item);
        GameRegistry.register(item);
    }


}
