package me.matl114.logitech.SlimefunItem.Blocks.MultiBlock.SmithWorkShop;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.SlimefunItem.AddSlimefunItems;
import me.matl114.logitech.SlimefunItem.Blocks.MultiBlockCore.MultiBlockPart;
import me.matl114.logitech.SlimefunItem.Machines.AbstractMachine;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class SmithingInterface extends AbstractMachine implements MultiBlockPart {
    private static final ConcurrentHashMap<Location, SlimefunItem> INTERFACE_MAP=new ConcurrentHashMap<>();

    private static int[] dx={1,0,-1,0};

    private static int[] dz={0,1,0,-1};
    public static Pair<Location,SlimefunItem> getAdjacentInterface(Location coreLocation){
        for (int i=0;i<4;++i){
            Location testLocation=coreLocation.clone().add(dx[i],0,dz[i]);
            if(INTERFACE_MAP.containsKey(testLocation)){
                return new Pair<>(testLocation,INTERFACE_MAP.get(testLocation));
            }
        }
        return null;
    }
    public static final SlimefunItem getInterface(Location location){
        return INTERFACE_MAP.get(location);
    }

    public static final Map<Location,SlimefunItem> getInterfaces(){
        return Collections.unmodifiableMap(INTERFACE_MAP);
    }
    /**
     * constructor of abstractMachines will keep Collections of MachineRecipes,will register energyNetwork params,
     * will set up menu by overriding constructMenu method
     *
     * @param category
     * @param item
     * @param recipeType
     * @param recipe
     * @param energybuffer
     * @param energyConsumption
     */
    private boolean redirectMenu;

    public SmithingInterface(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int energybuffer, int energyConsumption,boolean redirectMenu) {
        super(category, item, recipeType, recipe, energybuffer, energyConsumption);
        this.redirectMenu = redirectMenu;
    }
    @Override
    public void constructMenu(BlockMenuPreset preset) {
        IntStream.range(0,9).forEach(i->preset.addItem(i, ChestMenuUtils.getBackground(),ChestMenuUtils.getEmptyClickHandler()));
    }
    @Override
    public int[] getInputSlots() {
        return new int[0];
    }
    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }
    @Override
    public final String getPartId() {
        return "smith.interface";
    }
    @Override
	public void onMultiBlockBreak(BlockBreakEvent e){
        super.onBreak(e, DataCache.getMenu(e.getBlock().getLocation()));
        MultiBlockPart.super.onMultiBlockBreak(e);
        INTERFACE_MAP.remove(e.getBlock().getLocation());
    }
    @Override
    public void preRegister(){
        super.preRegister();
        handleMultiBlockPart(this);
    }
    @Override
    public void process(Block b, BlockMenu preset, SlimefunBlockData data) {

    }
    public void processFailed(Block b,BlockMenu menu,SlimefunBlockData data) {
        //implement logic here
    }
    public void processInterface(Block b,BlockMenu menu,SlimefunBlockData data,Location coreLocation,int speed) {
        //implement logic here
    }
    @Override
	public boolean redirectMenu(){
        return redirectMenu;
    }

    @Override
    public final void tick(Block b, BlockMenu menu, SlimefunBlockData data, int ticker){
        Location loc = b.getLocation();
        Location core= MultiBlockService.acceptPartRequest(loc);
        if(core!=null){
            INTERFACE_MAP.put(loc,this);
            if(DataCache.getSfItem(core) instanceof SmithingWorkshop ws){
                int speedLevel=ws.getAmplifyCompentLevel(core, AddSlimefunItems.SWAMP_SPEED);
                processInterface(b,menu,data,core,speedLevel);
            }

        }else{
            INTERFACE_MAP.remove(loc);
            processFailed(b, menu, data);
        }
        process(b,menu,data);
    }
}
