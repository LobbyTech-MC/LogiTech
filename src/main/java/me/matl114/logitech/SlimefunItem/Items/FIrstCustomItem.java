package me.matl114.logitech.SlimefunItem.Items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.matl114.logitech.SlimefunItem.AddItem;
import me.matl114.logitech.SlimefunItem.DistinctiveCustomItemStack;

public class FIrstCustomItem extends DistinctiveCustomItemStack {
    public FIrstCustomItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private void onItemUseRightClick(PlayerRightClickEvent event) {
        event.getPlayer().giveExpLevels(1);
        event.getPlayer().setItemInHand(AddItem.MATL114.clone());
    }
    @Override
    public void preRegister(){
        super.preRegister();
        addItemHandler((ItemUseHandler)this::onItemUseRightClick);
    }

}
