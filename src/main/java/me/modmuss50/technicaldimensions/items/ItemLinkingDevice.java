package me.modmuss50.technicaldimensions.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import me.modmuss50.technicaldimensions.TechnicalDimensions;
import me.modmuss50.technicaldimensions.client.ScreenShotUitls;
import me.modmuss50.technicaldimensions.client.gui.GuiLinkingDevice;
import me.modmuss50.technicaldimensions.init.ModItems;
import me.modmuss50.technicaldimensions.misc.LinkingIDHelper;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestTakeSS;
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
import reborncore.common.packets.PacketHandler;
import reborncore.common.util.ItemNBTHelper;
import reborncore.common.util.ItemUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark on 04/08/2016.
 */
public class ItemLinkingDevice extends Item implements ITexturedItem {


    public static ItemStack clientStack;

    public ItemLinkingDevice() {
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (itemStackIn.getItemDamage() == 1) {
            GuiLinkingDevice.heldStack = itemStackIn;
            playerIn.openGui(TechnicalDimensions.instance, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        } else {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setDouble("x", round(playerIn.prevPosX));
            compound.setDouble("y", round(playerIn.prevPosY));
            compound.setDouble("z", round(playerIn.prevPosZ));
            compound.setFloat("yaw", playerIn.rotationYaw);
            compound.setFloat("pitch", playerIn.rotationPitch);
            compound.setInteger("dim", playerIn.worldObj.provider.getDimension());
            if(!worldIn.isRemote){
                ItemNBTHelper.setCompound(itemStackIn, "tpData", compound);
                Optional<String> imageID = LinkingIDHelper.getIDFromStack(itemStackIn);
                if(imageID.isPresent()){
                    PacketHandler.sendPacketToPlayer(new PacketRequestTakeSS(imageID.get(), playerIn), playerIn);
                    compound.setString("imageID", imageID.get());
                }
            } else {
                clientStack = itemStackIn;
            }

            ItemNBTHelper.setCompound(itemStackIn, "tpData", compound);
            itemStackIn.setItemDamage(1);


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

    public static double round(double value){
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
