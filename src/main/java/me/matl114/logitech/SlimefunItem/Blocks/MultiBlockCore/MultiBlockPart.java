package me.matl114.logitech.SlimefunItem.Blocks.MultiBlockCore;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import me.matl114.logitech.Listeners.Listeners.BlockOpenListener;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.Utils.UtilClass.TickerClass.Ticking;

public interface MultiBlockPart extends Ticking {
    public String getPartId();
    /**
     * we override the handleBlock in @MenuBlock ,invoke this method in preRegister!
     * @param it
     */
    default void handleMultiBlockPart(SlimefunItem it){
        it.addItemHandler(new BlockBreakHandler(false, false) {
            @Override
			@ParametersAreNonnullByDefault
            public void onPlayerBreak(BlockBreakEvent e, ItemStack itemStack, List<ItemStack> list) {
                MultiBlockPart.this.onMultiBlockBreak(e);
            }
        });
    }
    default void onMenuRedirect(PlayerRightClickEvent event) {
        boolean itemUsed = event.getHand() == EquipmentSlot.OFF_HAND;


        if (!itemUsed && event.useBlock() != Event.Result.DENY && !BlockOpenListener.rightClickBlock(event)) {
            return;
        }
    }

    //you must add handle menu to preRegister in order to handle MultiBlockPart
    default void onMultiBlockBreak(BlockBreakEvent e) {
        String uid= MultiBlockService.safeGetUUID(e.getBlock().getLocation());
        MultiBlockService.deleteMultiBlock(uid, MultiBlockService.MultiBlockBreakCause.get(e.getBlock()));
    }
    default boolean redirectMenu(){
        return true;
    }
}
