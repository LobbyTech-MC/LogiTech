package me.matl114.logitech.utils.UtilClass.ItemClass;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.NotImplementedException;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MultiItemStack extends AbstractItemStack {
    public List<ItemStack> getItemStacks();
    default ItemStack clone()  {
        throw new NotImplementedException(".clone() method not implemented");
    }
    public int getTypeNum();
    public List<Double> getWeight(Double percent);

    public boolean matchItem(ItemStack item,boolean strictCheck);
}
