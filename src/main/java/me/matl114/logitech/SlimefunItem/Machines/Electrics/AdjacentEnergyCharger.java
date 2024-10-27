package me.matl114.logitech.SlimefunItem.Machines.Electrics;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.Interface.DirectionalBlock;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.UtilClass.CargoClass.Directions;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;

public class AdjacentEnergyCharger extends AbstractEnergyCharger implements DirectionalBlock {
    protected final int MAX_LEN=6;

    protected final String[] savedKeys = new String[]{
            "line_dir"
    };
    public  String[] getSaveKeys(){
        return savedKeys;
    }
    protected final int[] DIRECTION_SLOTS = new int[]{
            10
    };
    public int[] getDirectionSlots(){
        return DIRECTION_SLOTS;
    }
    public AdjacentEnergyCharger(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                             int energybuffer){
        super(category, item, recipeType, recipe, energybuffer);
    }
    public Collection<SlimefunBlockData> getChargeRange(BlockMenu inv, Block block, SlimefunBlockData data){
        Location loc=block.getLocation();
        Directions dir=getDirection(0,data);
        HashSet<SlimefunBlockData> ret=new HashSet<>();
        if(dir!=Directions.NONE){
            loc=dir.relate(loc);
            if(getChargeableComponent(DataCache.getSfItem(loc))!=null){
                ret.add(DataCache.safeLoadBlock(loc));
            }
        }else {
            for (Directions d : Directions.nonnullValues()) {
                Location testloc=d.relate(loc);
                if(getChargeableComponent(DataCache.getSfItem(testloc))!=null){
                    ret.add(DataCache.safeLoadBlock(testloc));
                }
            }
        }
        return ret;
    }
    public int getMaxChargeAmount(){
        return MAX_LEN;
    }
    public void newMenuInstance(BlockMenu menu, Block block){
        super.newMenuInstance(menu, block);
        menu.addMenuClickHandler(DIRECTION_SLOTS[0],getDirectionHandler(0,menu));
        updateDirectionSlots(0,menu);
    }
}