package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

/**
 * shaped and not stack ,should check your code before create this
 */
public class ShapedMachineRecipe extends MachineRecipe {
    public ShapedMachineRecipe(int ticks, ItemStack[] inputs, ItemStack outputs) {
        super(0, inputs, new ItemStack[] { outputs });
        this.setTicks(ticks);
    }
    public ShapedMachineRecipe(int ticks, ItemStack[] inputs, ItemStack[] outputs) {
        super(0, inputs, outputs);
        this.setTicks(ticks);
    }
}
