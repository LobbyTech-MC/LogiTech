package me.matl114.logitech.Utils.UtilClass.TickerClass;

import org.bukkit.block.Block;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class SyncBlockTick extends BlockTicker {
    public interface SyncTickers{
        public void syncTick(Block b, BlockMenu inv,SlimefunBlockData data,int synTickCount);
    }

    public static SyncBlockTick TESTINSTANCE = new SyncBlockTick();
    protected int tickCount=0;
    public SyncBlockTick(){

    }
    public boolean isSynchronized() {
        return false;
    }
    public void tick(Block b, SlimefunItem item, SlimefunBlockData data) {
       if(item instanceof SyncTickers){
           ((SyncTickers)item).syncTick(b,data.getBlockMenu(),data,tickCount);
       }
    }
    public void uniqueTick() {
        tickCount++;
    }
}
