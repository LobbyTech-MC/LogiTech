package me.matl114.logitech.Utils.UtilClass.StorageClass;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface LocationProxy {
    public int getAmount(Location loc) ;
    public ItemStack getItemStack(Location loc) ;
    public Location getLocation(ItemMeta meta) ;
    public int getMaxAmount(Location loc) ;
    public void setAmount(Location loc, int amount) ;
    public void updateLocation(Location loc);
}
