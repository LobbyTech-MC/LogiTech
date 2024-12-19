package me.matl114.logitech.SlimefunItem.Machines.Electrics;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.Machines.AbstractMachine;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class Capacitor extends AbstractMachine {
    public Capacitor(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                     int energybuffer){
        super(category,item,recipeType,recipe,energybuffer,0);
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
	public void preRegister(){
        registerTick(this);
        addInfo(this.getItem());
    }
    @Override
	public void process(Block b, BlockMenu inv, SlimefunBlockData data){
    }
}
