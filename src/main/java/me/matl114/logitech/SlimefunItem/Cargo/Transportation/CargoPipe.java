package me.matl114.logitech.SlimefunItem.Cargo.Transportation;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.Machines.Electrics.AbstractPipe;
import me.matl114.logitech.Utils.ContainerUtils;
import me.matl114.logitech.Utils.UtilClass.CargoClass.CargoConfigs;

public class CargoPipe extends AbstractPipe {
    private int configCode=CargoConfigs.getDefaultConfig();

    public  CargoPipe(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe){
        super(category, item, recipeType, recipe);
    }

    @Override
    public boolean avalibleDestination(Location toLocation) {
//        if(DataCache.getMenu(toLocation) != null){
//            return true;
//        }else {
//            return false;
//        }
        //for vanilla chests
        return true;
    }
    @Override
    public void transfer(Location from, Location to) {
       ContainerUtils.transferWithContainer(from,to,this.configCode,new HashSet<>(),false);
    }
}
