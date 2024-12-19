package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import org.bukkit.inventory.ItemStack;

public class MGeneratorRecipe extends StackMachineRecipe{
    public MGeneratorRecipe(int ticks, ItemStack[] input, ItemStack[] output){
        super(ticks,input,output);
    }
    public MGeneratorRecipe(StackMachineRecipe r){
        super(r.getTicks(),r.getInput(),r.getOutput());
    }
}
