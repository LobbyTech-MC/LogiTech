package me.matl114.logitech.core.Cargo.StorageMachines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

import me.matl114.logitech.core.Machines.Abstracts.AbstractMachine;

import me.matl114.logitech.utils.UtilClass.TickerClass.Ticking;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

import org.bukkit.block.Block;

import org.bukkit.inventory.ItemStack;

public class Storage extends AbstractMachine implements Ticking {
    public static final int[] COMMON_INPUT_SLOT = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
    public static final int[] COMMON_OUTPUT_SLOT = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
    protected final int[] INPUT_SLOT;
    protected final int[] OUTPUT_SLOT;
    public Storage(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,int[] inputs,int[] outputs){
        super(category,item , recipeType, recipe,0,0);
        this.INPUT_SLOT = inputs;
        this.OUTPUT_SLOT = outputs;
    }
    public void addInfo(ItemStack item){
    }
    public void constructMenu(BlockMenuPreset preset){
        preset.setSize(54);
    }


    public  int[] getInputSlots(){
        return INPUT_SLOT;
    }

    public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }
    public void preRegister(){
        super.preRegister();

    }
    public void  process(Block b,BlockMenu inv,SlimefunBlockData data){

    }
}
