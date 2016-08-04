package me.modmuss50.technicaldimensions.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import me.modmuss50.technicaldimensions.TechnicalDimensions;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by Mark on 04/08/2016.
 */
public class ItemLinkingDevice extends Item implements ITexturedItem {

    public ItemLinkingDevice() {
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        GuiLinkingDevice.heldStack = itemStackIn;

        playerIn.openGui(TechnicalDimensions.instance, 0, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public String getTextureName(int damage) {
        return "technicaldimensions:items/linking_on";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation("technicaldimensions:" + getUnlocalizedName(stack).substring(5));
    }
}
