package me.matl114.logitech.SlimefunItem.Blocks;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.DistinctiveCustomItemStack;
import me.matl114.logitech.SlimefunItem.Interface.MenuBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class AbstractBlock extends DistinctiveCustomItemStack implements MenuBlock {
    public AbstractBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }
    @Override
	public void addInfo(ItemStack stack){

    }
    @Override
	public void constructMenu(BlockMenuPreset preset){

    }
    @Override
	public int[] getInputSlots(){
        return new int[0];
    }
    @Override
	public int[] getOutputSlots(){
        return new int[0];
    }
    @Override
	public void preRegister() {
        super.preRegister();
        this.handleBlock(this);
    }


}
