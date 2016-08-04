package me.modmuss50.technicaldimensions.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.init.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import reborncore.common.util.ItemNBTHelper;
import reborncore.common.util.ItemUtils;

/**
 * Created by Mark on 04/08/2016.
 */
public class ItemUnlinkedDevice extends Item implements ITexturedItem {
    public ItemUnlinkedDevice() {

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ScreenShotUitls.takeScreenShot(itemStackIn, playerIn);
        ItemStack newStack = new ItemStack(ModItems.linkingDevice);
        ItemNBTHelper.initNBT(newStack);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setDouble("x", playerIn.posX);
        compound.setDouble("y", playerIn.posY);
        compound.setDouble("z", playerIn.posZ);
        compound.setFloat("yaw", playerIn.cameraYaw);
        compound.setFloat("pitch", playerIn.cameraPitch);
        compound.setInteger("dim", playerIn.worldObj.provider.getDimension());
        ItemNBTHelper.setCompound(newStack, "tpData", compound);
        return new ActionResult<>(EnumActionResult.SUCCESS, newStack);
    }

    @Override
    public String getTextureName(int damage) {
        return "technicaldimensions:items/linking_off";
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
