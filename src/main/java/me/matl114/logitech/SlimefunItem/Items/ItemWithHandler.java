package me.matl114.logitech.SlimefunItem.Items;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public abstract class ItemWithHandler<T extends ItemHandler> extends ItemNotPlaceable{
    public ItemWithHandler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe){
        super(itemGroup, item, recipeType, recipe);
    }
    public ItemWithHandler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, List<ItemStack> displayes){
        super(itemGroup, item, recipeType, recipe);
        setDisplayRecipes(displayes);
    }
    public abstract T[] getItemHandler();
    @Override
	public void preRegister(){
        super.preRegister();
        addItemHandler(getItemHandler());
    }
}
