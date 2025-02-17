package me.matl114.logitech.Utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import me.matl114.logitech.Manager.Schedules;
import me.matl114.logitech.core.CustomSlimefunItem;
import me.matl114.logitech.Utils.UtilClass.CargoClass.ContainerBlockMenuWrapper;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ContainerUtils {
    public static void setup(){
        ContainerBlockMenuWrapper.setup();
    }
    private static int[][] preCalculatedSlots= IntStream.range(0,73).mapToObj(i->{
        return IntStream.range(0,i).toArray();
    }).toArray(int[][]::new);
    private static int[] getSlotAccess(int size){
        if(preCalculatedSlots[size]==null){
            return IntStream.range(0,size).toArray();
        }else return preCalculatedSlots[size];
    }
    @Getter
    protected static BlockMenuPreset containerWrapperMenuPreset =new BlockMenuPreset("LOGITECH_FUNCTIONAL_BLOCKMENU","&c容器") {
        @Override
        public void init() {
            for(int i=0;i<54;++i){
                this.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
            }
            this.setSize(54);
        }
        @Override
        public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
            return false;
        }

        @Override
        public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
            return new int[0];
        }
        public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
            if(menu instanceof ContainerBlockMenuWrapper impl){
                return getSlotAccess(impl.getSlotSize());
            }else return getSlotsAccessedByItemTransport(flow);
        };
        public void newInstance(@Nonnull BlockMenu menu, @Nonnull Location l) {
            //do nothing
        }
    };
    /**
     * run callback on MainThread
     * processing Block Inventory in Async thread may trigger sync event which throws IllegalStateException
     * @param callback
     * @param testBlock
     */

    //todo test method safety not in main
    public static void getBlockContainerMenuWrapperAsyncCallback(Consumer<BlockMenu[]> callback, boolean runningOnMain, Location... testBlock){
        TileState[] states = new TileState[testBlock.length];
        BlockMenu[] results=new BlockMenu[testBlock.length];
        Schedules.execute(()->{
            int len = testBlock.length;
            for(int i=0;i<len;++i){
                if(testBlock[i]!=null){
                    BlockState state =   testBlock[i].getBlock().getState(false);
                    if(state instanceof TileState tile && state instanceof InventoryHolder holder){
                        Inventory inventory = holder.getInventory();
                       if(runningOnMain||me.matl114.matlib.Utils.WorldUtils.isInventoryTypeAsyncSafe(inventory.getType())){
                            results[i]= ContainerBlockMenuWrapper.getContainerBlockMenu(inventory,testBlock[i]);
                            states[i] = tile;
                            continue;
                        }
                    }
                }
                results[i]=null;
                states[i] = null;
            }
            if(runningOnMain){
                //running on mainThread
                callback.accept(results);
            }else {
                //not running on mainThread
                Schedules.launchSchedules(()->{
                    //test tileState removal
                    for(int i=0;i<len;++i){
                        TileState tileState = states[i];
                        //not valid anymore,set to null
                        if(tileState!=null && !me.matl114.matlib.Utils.WorldUtils.isTileEntityStillValid(tileState)){
                            states[i] = null;
                            results[i] = null;
                        }
                    }
                    callback.accept(results);
                },0,false,0);
            }
        },true);
    }
    //todo add InventoryType slotAccess
    public static void transferWithContainer(Location from, Location to, int configCode, HashSet<ItemStack> bwlist,boolean smart){
        BlockMenu fromInv=DataCache.getMenu(from);

        BlockMenu toInv=DataCache.getMenu(to);
        if(fromInv!=null&&toInv!=null){
            TransportUtils.transportItem(fromInv,toInv,configCode,smart,bwlist,CraftUtils.getpusher);
            return;
        }
        if(!from.getWorld().isChunkLoaded(from.getBlockX()>>4,from.getBlockZ()>>4)||!to.getWorld().isChunkLoaded(to.getBlockX()>>4,to.getBlockZ()>>4)){
            //stop transfering to vanilla container when one chunk is not loaded
            return;
        }
        SlimefunItem fromItem=DataCache.getSfItem(from);
        SlimefunItem toItem =DataCache.getSfItem(to);
        //null, or not my machine
        if((fromInv!=null&&(!(toItem instanceof CustomSlimefunItem)))||(toInv!=null&&(!(fromItem instanceof CustomSlimefunItem)) )||(fromInv==null&&toInv==null) ) {
            if(fromInv!=null){
                ContainerUtils.getBlockContainerMenuWrapperAsyncCallback((blockMenus -> {
                    if(blockMenus[0]!=null){
                        TransportUtils.transportItem(fromInv,blockMenus[0],configCode,smart,bwlist,CraftUtils.getpusher);
                    }

                }),false, to);
            }else {
                if(toInv!=null){
                    ContainerUtils.getBlockContainerMenuWrapperAsyncCallback((blockMenus -> {
                        if(blockMenus[0]!=null)
                            TransportUtils.transportItem(blockMenus[0],toInv,configCode,smart,bwlist,CraftUtils.getpusher);
                    }),false, from);
                }else {
                    ContainerUtils.getBlockContainerMenuWrapperAsyncCallback((blockMenus -> {
                        if(blockMenus[0]!=null&&blockMenus[1]!=null){
                            TransportUtils.transportItem(blockMenus[0],blockMenus[1],configCode,smart,bwlist,CraftUtils.getpusher);
                        }
                    }),false, from,to);
                }
            }
        }
    }
    public static BlockMenu getPlayerBackPackWrapper(Player p){
        return ContainerBlockMenuWrapper.getContainerBlockMenu(p.getInventory(),p.getLocation(),36);
    }
    public static BlockMenu getPlayerInventoryWrapper(Player p){
        return ContainerBlockMenuWrapper.getContainerBlockMenu(p.getInventory(),p.getLocation());
    }
}
