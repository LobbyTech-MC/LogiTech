package me.matl114.logitech.SlimefunItem.Machines.Electrics;

import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.Machines.AbstractEnergyProcessor;

public class EGenerator extends AbstractEnergyProcessor {
    protected final int[] INPUT_SLOT=new int[]{
            19,20
    };
    protected final int[] OUTPUT_SLOT=new int[]{
            24,25
    };
    public EGenerator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Material progressItem,
                      int energyBuffer, int energyProvider, LinkedHashMap<Object,Integer> customRecipe) {
        super(category,item,recipeType,recipe,progressItem,energyBuffer,energyProvider,customRecipe);
    }
    @Override
	public int[] getInputSlots(){
        return INPUT_SLOT;
    }
    @Override
	public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }
}
