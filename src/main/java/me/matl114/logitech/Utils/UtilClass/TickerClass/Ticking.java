package me.matl114.logitech.Utils.UtilClass.TickerClass;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public interface Ticking {
    default boolean isSync(){
        return  false;
    }
    /**
     * call this method in preRegister
     * @param item
     */
    default void registerTick(SlimefunItem item){
        item.addItemHandler(
                new BlockTicker() {
                    @Override
					public boolean isSynchronized() {
                        return Ticking.this.isSync();
                    }

                    @Override
					@ParametersAreNonnullByDefault
                    public void tick(Block b, SlimefunItem item, SlimefunBlockData data) {
                        BlockMenu menu = data.getBlockMenu();
                        //BlockMenu menu = BlockStorage.getInventory(b);

                        Ticking.this.tick(b, menu,data,0);


                    }
                }


        );
    }
    default void tick(Block b, @Nullable BlockMenu menu, int tickCount){

    }
    /**
     * imple blockTicking in this method
     * menu could be null, unless you guarantee you imple MenuBlock
     * @param b
     * @param menu
     * @param tickCount
     */
    default void tick(Block b, @Nullable BlockMenu menu, SlimefunBlockData data,int tickCount){
        tick(b,menu,tickCount);
    }

}
