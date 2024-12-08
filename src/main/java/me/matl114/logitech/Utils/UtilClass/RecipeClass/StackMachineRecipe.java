package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

/**
 * unshaped recipe with stacked input ItemStack can be used in most Abstract machines
 */
public class StackMachineRecipe extends MachineRecipe {
    public StackMachineRecipe(int ticks, ItemStack[] inputs, ItemStack[] outputs) {
        super(0, inputs, outputs);
        this.setTicks(ticks);
    }
}
