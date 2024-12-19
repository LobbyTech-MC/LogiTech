package me.matl114.logitech.SlimefunItem.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import me.matl114.logitech.SlimefunItem.AddSlimefunItems;
import me.matl114.logitech.SlimefunItem.Interface.RecipeDisplay;
import me.matl114.logitech.Utils.AddUtils;
import me.matl114.logitech.Utils.Debug;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

public class MyVanillaItem extends VanillaItem implements RecipeDisplay {
    List<ItemStack> displayedMemory;
    public MyVanillaItem(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
        this.useableInWorkbench = false;
    }
    public MyVanillaItem addDisplayRecipe(ItemStack stack) {
        if(displayedMemory==null||displayedMemory.isEmpty()){
            this.displayedMemory=new ArrayList<>();
        }
        this.displayedMemory.add(stack);
        return this;
    }
    public MyVanillaItem addHandler(ItemHandler handler) {
        this.addItemHandler(handler);
        return this;
    }
    @Override
	public final List<ItemStack> getDisplayRecipes() {
        if(displayedMemory==null||displayedMemory.isEmpty()) {
            displayedMemory=_getDisplayRecipes(new ArrayList<>());
        }
        return displayedMemory;
    }
    @Override
	public void postRegister(){
        super.postRegister();
        this.addWikiPage("");
    }
    @Override
	public List<MachineRecipe> provideDisplayRecipe(){
        return new ArrayList<>();
    }
    public MyVanillaItem register(){
        if(AddSlimefunItems.INSTANCE!=null){
            register(AddSlimefunItems.INSTANCE);
        }else{
            Debug.logger("找不到附属实例!  注册信息: "+this.toString());
        }
        return this;
    }
    public MyVanillaItem setDisplayRecipes(List<ItemStack> displayRecipes) {
        this.displayedMemory = displayRecipes;
        return this;
    }
    public MyVanillaItem setOutput(Object obj){
        this.recipeOutput= AddUtils.resolveItem(obj);
        return this;
    }


}
