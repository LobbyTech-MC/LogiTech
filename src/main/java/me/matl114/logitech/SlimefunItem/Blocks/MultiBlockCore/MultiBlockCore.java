package me.matl114.logitech.SlimefunItem.Blocks.MultiBlockCore;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.matl114.logitech.SlimefunItem.Interface.MenuBlock;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.Debug;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockType;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.Utils.UtilClass.TickerClass.Ticking;

public interface MultiBlockCore extends MultiBlockPart, Ticking , MenuBlock {
    /**
     * only called when in tickers if status down*****
     * @param loc
     * @param data
     * @param autoCode
     */
    static ConcurrentHashMap<Location, AtomicBoolean> locks=new ConcurrentHashMap<>();
    default void autoBuild(Location loc, SlimefunBlockData data, int autoCode){
        if(autoCode<=0) {
			return;
		}
        if(autoCode==3){//3tick重连一次
            Location tarloc=loc.clone();
            AtomicBoolean lock=locks.computeIfAbsent(tarloc,(i)->new AtomicBoolean(false));
            if(lock.compareAndSet(false,true)){
                CompletableFuture.runAsync(()->{
                    try{
                        MultiBlockService.createNewHandler(loc,getBuilder(),getMultiBlockType());
                    }finally{//
                        //secure lockers
                        lock.set(false);
                    }
                }
                );
            }
            autoCode=1;
        }else {
            autoCode+=1;
        }
        data.setData(MultiBlockService.getAutoKey(),String.valueOf(autoCode));
    }

    default MultiBlockService.MultiBlockBuilder getBuilder(){
        return MultiBlockHandler::createHandler;
    }
    public AbstractMultiBlockType getMultiBlockType();
    @Override
	default void handleBlock(SlimefunItem machine){
        machine.addItemHandler(
            new BlockBreakHandler(false, false) {
                @Override
				@ParametersAreNonnullByDefault
                public void onPlayerBreak(BlockBreakEvent e, ItemStack itemStack, List<ItemStack> list) {
                    //BlockMenu menu = DataCache.getMenu(e.getBlock().getLocation());// BlockStorage.getInventory(e.getBlock());
                    MultiBlockCore.this.onMultiBlockBreak(e);
                }
            }, new BlockPlaceHandler(false) {
                @Override
				@ParametersAreNonnullByDefault
                public void onPlayerPlace(BlockPlaceEvent e) {
                    MultiBlockCore.this.onPlace(e, e.getBlockPlaced());
                }
            });
    }
    /**
     * should override if Menu if present ,should add MenuBlock.onBreak
     * @param e
     */
    @Override
	default void onMultiBlockBreak(BlockBreakEvent e) {
        this.onBreak(e,DataCache.getMenu(e.getBlock().getLocation()));
        MultiBlockPart.super.onMultiBlockBreak(e);
        Location loc = e.getBlock().getLocation();
        MultiBlockService.removeHologram(loc);
        locks.remove(loc);
    }
    default  void onMultiBlockDisable(Location loc, AbstractMultiBlockHandler handler, MultiBlockService.DeleteCause cause){
        Debug.debug(cause.getMessage());
    }
    default void onMultiBlockEnable(Location loc,AbstractMultiBlockHandler handler){
        MultiBlockService.removeHologram(loc);
    }

    @Override
	default boolean redirectMenu(){
        return false;
    }

    /**
     * only called when in tickers to randomly check completeness when status on.if down,send to handler and set Active to false,
     * machine will shut down at next tick
     */
    default void runtimeCheck(Location loc,SlimefunBlockData data,int autoCode){
        int sgn=autoCode>0?1:-1;
        if(autoCode*sgn==3){//3tick检测一次
            Location tarloc=loc.clone();
            AtomicBoolean lock=locks.computeIfAbsent(tarloc,(i)->new AtomicBoolean(false));
            if(lock.compareAndSet(false,true)){
                CompletableFuture.runAsync(()->{
                    try{
                        MultiBlockService.checkIfAbsentRuntime(data);
                    }finally{//
                        //secure lockers
                       lock.set(false);
                    }
                }
                );
            }
            autoCode=sgn;
        }else {
            autoCode+=sgn;
        }
        data.setData(MultiBlockService.getAutoKey(),String.valueOf(autoCode));
    }
}
