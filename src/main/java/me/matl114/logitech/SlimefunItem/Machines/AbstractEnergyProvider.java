package me.matl114.logitech.SlimefunItem.Machines;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.Interface.EnergyProvider;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class AbstractEnergyProvider extends AbstractMachine implements EnergyProvider {
    public AbstractEnergyProvider(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                                  int energyBuffer, int energyProvider) {
        super(category, item, recipeType, recipe,   energyBuffer,-energyProvider);
    }
    @Override
	public abstract int getGeneratedOutput(@Nonnull Location l, @Nonnull SlimefunBlockData data);
    @Override
	public void preRegister(){
        super.preRegister();
    }
    @Override
	public void process(Block b, BlockMenu inv, SlimefunBlockData data){
    }
    @Override
	public void registerTick(SlimefunItem item){
        //no ticker
    }
}
