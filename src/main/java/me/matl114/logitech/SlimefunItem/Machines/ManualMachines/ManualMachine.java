package me.matl114.logitech.SlimefunItem.Machines.ManualMachines;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.Schedule.SchedulePostRegister;
import me.matl114.logitech.SlimefunItem.Machines.AbstractManual;
import me.matl114.logitech.Utils.UtilClass.RecipeClass.ImportRecipes;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

public class ManualMachine extends AbstractManual implements ImportRecipes {
    public  List<ItemStack> displayedMemory = null;
    public ManualMachine(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                         int energybuffer, int energyConsumption,
                         Supplier<List<MachineRecipe>> machineRecipeSupplier) {
        super(category,item,recipeType,recipe,energybuffer,energyConsumption,null);
        this.machineRecipeSupplier = machineRecipeSupplier;
        //开服之后加载配方
        SchedulePostRegister.addPostRegisterTask(()->{
            getDisplayRecipes();
        });
    }
    public void registerDefaultRecipes(){
    }
}
