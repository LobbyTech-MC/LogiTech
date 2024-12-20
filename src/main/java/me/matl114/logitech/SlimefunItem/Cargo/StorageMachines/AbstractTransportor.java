package me.matl114.logitech.SlimefunItem.Cargo.StorageMachines;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import me.matl114.logitech.SlimefunItem.DistinctiveCustomItemStack;
import me.matl114.logitech.SlimefunItem.Interface.MenuBlock;
import me.matl114.logitech.Utils.Settings;
import me.matl114.logitech.Utils.UtilClass.TickerClass.Ticking;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public abstract class AbstractTransportor extends DistinctiveCustomItemStack implements Ticking, MenuBlock, NotHopperable{
    public AbstractTransportor(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe){
        super(category,item , recipeType, recipe);
    }
    @Override
	public void addInfo(ItemStack stack){
    }
    @Override
	public abstract void constructMenu(BlockMenuPreset preset);
    @Override
	public abstract int[] getInputSlots();
    @Override
	public abstract int[] getOutputSlots();
    @Override
	public void newMenuInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block){
        updateMenu(blockMenu,block,Settings.INIT);
    }
    @Override
	public void preRegister(){
        super.preRegister();
        //
        registerTick(this);
        //为menublock提供 需要
        registerBlockMenu(this);
    }
    @Override
	public abstract void tick(Block b, BlockMenu menu, SlimefunBlockData data, int ticker);
    public void updateMenu(BlockMenu blockMenu, Block block, Settings mod){
    }
}
