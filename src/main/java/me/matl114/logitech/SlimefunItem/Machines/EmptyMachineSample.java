package me.matl114.logitech.SlimefunItem.Machines;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.Utils.Settings;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public  class EmptyMachineSample extends AbstractMachine{
    protected final int[] INPUT_SLOTS=new int[0];
    protected final int[] OUTPUT_SLOTS=new int[0];
    public EmptyMachineSample(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                           int energybuffer, int energyConsumption){
        super(category, item, recipeType, recipe, energybuffer, energyConsumption);
    }
    @Override
	public void constructMenu(BlockMenuPreset preset){

    }

    @Override
	public int[] getInputSlots(){
        return INPUT_SLOTS;
    }
    @Override
	public int[] getOutputSlots(){
        return OUTPUT_SLOTS;
    }
    @Override
	public void newMenuInstance(BlockMenu menu, Block block){
    }
    @Override
	public void process(Block b, BlockMenu menu, SlimefunBlockData data){}
    @Override
	public void updateMenu(BlockMenu menu, Block block, Settings mod){}
}
