package me.matl114.logitech.SlimefunItem.Cargo.Config;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.DistinctiveCustomSlimefunItem;
import org.bukkit.inventory.ItemStack;

public class ConfigCard extends DistinctiveCustomSlimefunItem {
    public ConfigCard(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe){
        super(itemGroup, item, recipeType, recipe);
    }
}
