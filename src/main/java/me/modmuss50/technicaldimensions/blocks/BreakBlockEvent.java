package me.modmuss50.technicaldimensions.blocks;

import me.modmuss50.technicaldimensions.init.ModBlocks;
import me.modmuss50.technicaldimensions.tiles.TileTeleporter;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by Mark on 06/08/2016.
 */
public class BreakBlockEvent {

    @SubscribeEvent
    public static void breakBlockEvent(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && !(event.getPlayer() instanceof FakePlayer) && event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.teleporter) {
            event.setCanceled(true);
        }
    }

}
