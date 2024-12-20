package me.matl114.logitech.SlimefunItem.Items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.matl114.logitech.Schedule.PersistentEffects.CustomEffects;
import me.matl114.logitech.Schedule.PersistentEffects.PlayerEffects;
import me.matl114.logitech.SlimefunItem.DistinctiveCustomItemStack;

public class AntiGravityBar extends DistinctiveCustomItemStack {
    public AntiGravityBar(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }
    public boolean onItemClick(PlayerRightClickEvent e){

        PlayerEffects.grantEffect(CustomEffects.ANTI_GRAVITY,e.getPlayer(),1,10);
        return false;
    }
    @Override
	public void preRegister(){
        this.addHandler((ItemUseHandler)this::onItemClick);
    }
}
