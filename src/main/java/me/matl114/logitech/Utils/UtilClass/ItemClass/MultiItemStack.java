package me.matl114.logitech.Utils.UtilClass.ItemClass;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.NotImplementedException;

public interface MultiItemStack extends AbstractItemStack {
    default ItemStack clone()  {
        throw new NotImplementedException(".clone() method not implemented");
    }
    public List<ItemStack> getItemStacks();
    public int getTypeNum();
    public List<Double> getWeight(Double percent);

    public boolean matchItem(ItemStack item,boolean strictCheck);
}
