package me.modmuss50.technicaldimensions.misc;

import me.modmuss50.technicaldimensions.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.codec.digest.DigestUtils;
import reborncore.common.util.ItemNBTHelper;

import java.util.Optional;

/**
 * Created by Mark on 04/08/2016.
 */
public class LinkingIDHelper {

    public static Optional<String> getIDFromStack(ItemStack stack) {
        if (stack.getItem() != ModItems.linkingDevice) {
            return Optional.empty();
        }
        NBTTagCompound compound = ItemNBTHelper.getCompound(stack, "tpData", true);
        if (compound == null) {
            return Optional.empty();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(compound.getInteger("dim"));
        sb.append(compound.getDouble("x"));
        sb.append(compound.getDouble("y"));
        sb.append(compound.getDouble("z"));
        sb.append(compound.getFloat("yaw"));
        sb.append(compound.getFloat("pitch"));
        String data = sb.toString();
        String md5 = DigestUtils.md5Hex(data);
        return Optional.of(md5);
    }
}
