package me.modmuss50.technicaldimensions.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import me.modmuss50.technicaldimensions.TechnicalDimensions;
import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.misc.LinkingIDHelper;
import me.modmuss50.technicaldimensions.misc.TeleportationUtils;
import me.modmuss50.technicaldimensions.packets.PacketUtill;
import me.modmuss50.technicaldimensions.packets.screenshots.PacketRequestTakeSS;
import me.modmuss50.technicaldimensions.tiles.TilePortalController;
import me.modmuss50.technicaldimensions.world.DimData;
import me.modmuss50.technicaldimensions.world.ModDimensions;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import reborncore.common.util.ItemNBTHelper;

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
        if (itemStackIn.getItemDamage() == 2) {
//            TechnicalDimensions.proxy.setInuseStack(itemStackIn);
//            playerIn.openGui(TechnicalDimensions.instance, 1, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            if (!worldIn.isRemote) {
                if (ItemNBTHelper.getCompound(itemStackIn, "dimData", true) == null) {
                    DimData data = new DimData(ModDimensions.getNextDimName());
                    itemStackIn.setStackDisplayName(data.name);
                    int id = ModDimensions.createOrGetDim(data);
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setInteger("dimID", id);
                    compound.setString("dimName", data.name);
                    ItemNBTHelper.setCompound(itemStackIn, "dimData", compound);
                    TeleportationUtils.teleportEntity(playerIn, 0, 0, 0, 0, 0, id, true);
                } else {
                    NBTTagCompound compound = ItemNBTHelper.getCompound(itemStackIn, "dimData", true);
                    int dimId = compound.getInteger("dimID");
                    TeleportationUtils.teleportEntity(playerIn, 0, 0, 0, 0, 0, dimId, true);
                }

            }
        } else if (itemStackIn.getItemDamage() == 1) {
            TechnicalDimensions.proxy.setInuseStack(itemStackIn);
            playerIn.openGui(TechnicalDimensions.instance, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        } else {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setDouble("x", round(playerIn.prevPosX));
            compound.setDouble("y", round(playerIn.prevPosY));
            compound.setDouble("z", round(playerIn.prevPosZ));
            compound.setFloat("yaw", playerIn.rotationYaw);
            compound.setFloat("pitch", playerIn.rotationPitch);
            compound.setInteger("dim", playerIn.worldObj.provider.getDimension());
            if (!worldIn.isRemote) {
                ItemNBTHelper.setCompound(itemStackIn, "tpData", compound);
                Optional<String> imageID = LinkingIDHelper.getIDFromStack(itemStackIn);
                if (imageID.isPresent()) {
                    PacketUtill.INSTANCE.sendTo(new PacketRequestTakeSS(imageID.get()), (EntityPlayerMP) playerIn);
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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (stack.getItemDamage() == 1 && worldIn.getBlockState(pos).getBlock() == ModBlocks.portalController) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TilePortalController) {
                NBTTagCompound compound = ItemNBTHelper.getCompound(stack, "tpData", true);
                if (compound != null) {
                    TilePortalController controller = (TilePortalController) tile;
                    controller.imageID = compound.getString("imageID");
                    controller.x = compound.getDouble("x");
                    controller.y = compound.getDouble("y");
                    controller.z = compound.getDouble("z");
                    controller.yaw = compound.getFloat("yaw");
                    controller.pitch = compound.getFloat("pitch");
                    controller.dim = compound.getInteger("dim");
                    return EnumActionResult.SUCCESS;
                }

            }
        }
        return EnumActionResult.FAIL;
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
        } else if (stack.getItemDamage() == 2) {
            tooltip.add(TextFormatting.RED + "THIS IS PROOF OF CONCEPT!!!");
            tooltip.add(TextFormatting.RED + "WHEN ITS DONE YOU WILL BE ABLE TO CUSTOMIZE THE WORLD");
            tooltip.add(TextFormatting.RED + "ITS NOW A FLAT WORLD - FULL DAY");
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        subItems.add(new ItemStack(itemIn, 1, 2));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + this.getUnlocalizedName() + (stack.getItemDamage() == 1 ? ".on" : stack.getItemDamage() == 2 ? ".dim" : ".off");
    }

    @Override
    public String getTextureName(int damage) {
        if (damage == 2) {
            return "technicaldimensions:items/linking_dim";
        }
        if (damage == 1) {
            return "technicaldimensions:items/linking_on";
        }
        return "technicaldimensions:items/linking_off";
    }

    @Override
    public int getMaxMeta() {
        return 3;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation("technicaldimensions:" + getUnlocalizedName(stack).substring(5));
    }

    public static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
