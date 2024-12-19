package me.matl114.logitech.SlimefunItem.Machines.Electrics;


import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class EnergyTrash  extends Capacitor {
    public EnergyTrash(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                       int energybuffer) {
        super(category, item, recipeType, recipe, energybuffer);
    }
    @Override
	public void addCharge(@Nonnull Location l, int charge){
    }
    @Override
	public int getCharge(@Nonnull Location l) {
        return 0;
    }
    /** @deprecated */
    @Override
	@Deprecated
    public int getCharge(@Nonnull Location l, @Nonnull Config config) {
        return 0;
    }
    @Override
	public int getCharge(@Nonnull Location l, @Nonnull SlimefunBlockData data) {
        return 0;
    }
    @Override
	public void process(Block b, BlockMenu inv, SlimefunBlockData data){
    }

    @Override
	public void removeCharge(@Nonnull Location l, int charge){
    }

    @Override
	public void setCharge(@Nonnull Location l, int charge){

    }
}
