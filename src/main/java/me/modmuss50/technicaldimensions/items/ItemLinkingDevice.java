package me.modmuss50.technicaldimensions.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import me.modmuss50.technicaldimensions.TechnicalDimensions;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import me.modmuss50.technicaldimensions.init.ModItems;
import me.modmuss50.technicaldimensions.misc.LinkingIDHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
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

import java.util.List;

/**
 * Created by Mark on 04/08/2016.
 */
public class ItemLinkingDevice extends Item implements ITexturedItem {
    public ItemLinkingDevice() {
        setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (itemStackIn.getItemDamage() == 1) {
            GuiLinkingDevice.heldStack = itemStackIn;
            playerIn.openGui(TechnicalDimensions.instance, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        } else {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setDouble("x", playerIn.posX);
            compound.setDouble("y", playerIn.posY);
            compound.setDouble("z", playerIn.posZ);
            compound.setFloat("yaw", playerIn.rotationYaw);
            compound.setFloat("pitch", playerIn.rotationPitch);
            compound.setInteger("dim", playerIn.worldObj.provider.getDimension());

            ItemNBTHelper.setCompound(itemStackIn, "tpData", compound);
            itemStackIn.setItemDamage(1);

            if (worldIn.isRemote) {
                ScreenShotUitls.takeScreenShot(itemStackIn, playerIn);
            }

            System.out.println(LinkingIDHelper.getIDFromStack(itemStackIn).get().toString());
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (stack.getItemDamage() == 1) {
            NBTTagCompound compound = ItemNBTHelper.getCompound(stack, "tpData", true);
            if (compound != null) {
                tooltip.add("Dim: " + compound.getInteger("dim"));
                tooltip.add("X: " + compound.getDouble("x"));
                tooltip.add("Y: " + compound.getDouble("y"));
                tooltip.add("Z: " + compound.getDouble("z"));
                tooltip.add("Yaw: " + compound.getFloat("yaw"));
                tooltip.add("Pitch: " + compound.getFloat("pitch"));
            }
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        subItems.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + this.getUnlocalizedName() + (stack.getItemDamage() == 1 ? ".on" : ".off");
    }

    @Override
    public String getTextureName(int damage) {
        if (damage == 1) {
            return "technicaldimensions:items/linking_on";
        }
        return "technicaldimensions:items/linking_off";
    }

    @Override
    public int getMaxMeta() {
        return 2;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation("technicaldimensions:" + getUnlocalizedName(stack).substring(5));
    }
}
