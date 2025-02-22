package me.matl114.logitech.core.Machines.Abstracts;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.core.Interface.EnergyProvider;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public abstract class AbstractEnergyProvider extends AbstractMachine implements EnergyProvider {
    public AbstractEnergyProvider(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                                  int energyBuffer, int energyProvider) {
        super(category, item, recipeType, recipe,   energyBuffer,-energyProvider);
    }
    public void process(Block b, BlockMenu inv, SlimefunBlockData data){
    }
    public void registerTick(SlimefunItem item){
        //no ticker
    }
    public abstract int getGeneratedOutput(@Nonnull Location l, @Nonnull SlimefunBlockData data);
    public void preRegister(){
        super.preRegister();
    }
}
