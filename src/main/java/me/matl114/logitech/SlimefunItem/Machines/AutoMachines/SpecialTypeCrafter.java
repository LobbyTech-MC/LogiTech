package me.matl114.logitech.SlimefunItem.Machines.AutoMachines;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.Utils.RecipeSupporter;

public class SpecialTypeCrafter extends SpecialCrafter {
    public SpecialTypeCrafter(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                          Material progressItem, int ticks, int energyConsumption, int energyBuffer, HashSet<RecipeType> blackList){
        super(category, item, recipeType, recipe, progressItem, ticks, energyConsumption, energyBuffer,blackList);
    }
    public boolean advanced(){
        return false;
    }
    @Override
    public HashMap<SlimefunItem, RecipeType> getRecipeTypeMap() {
        return RecipeSupporter.CUSTOM_RECIPETYPES;
    }
}
