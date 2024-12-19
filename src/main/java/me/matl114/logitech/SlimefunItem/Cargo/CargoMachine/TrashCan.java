package me.matl114.logitech.SlimefunItem.Cargo.CargoMachine;

import java.util.stream.IntStream;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.SlimefunItem.Machines.AbstractMachine;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class TrashCan extends AbstractMachine {
    protected final int[] INPUT_SLOTS= IntStream.rangeClosed(0, 53).toArray();
    protected final int[] OUTPUT_SLOTS= new int[0];
    public TrashCan(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category,item,recipeType,recipe,0,0);
    }
    @Override
	public void constructMenu(BlockMenuPreset preset){
        preset.setSize(54);
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
	public void process(Block b, BlockMenu preset, SlimefunBlockData data){

    }
    @Override
	public void tick(Block b, BlockMenu menu, SlimefunBlockData data, int ticker) {
        //Schedules.launchSchedules(()->{
            int[] slots=getInputSlots();
            ItemStack it;
            for (int slot : slots) {
                if(menu.getItemInSlot(slot)==null) {
					break;
				}
                menu.replaceExistingItem(slot,null,false);
            }
        //},0,false,0);
    }
}
