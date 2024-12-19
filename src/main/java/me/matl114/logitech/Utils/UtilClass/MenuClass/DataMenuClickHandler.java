package me.matl114.logitech.Utils.UtilClass.MenuClass;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public interface DataMenuClickHandler extends ChestMenu.MenuClickHandler {
    default int getInt(int val){
        return 0;
    }
    default ItemStack getItemStack(int val){
        return null;
    }
    default Location getLocation(int val){
        return null;
    }
    default Object getObject(int val){
        return null;
    }
    default String getString(int val){
        return "";
    }
    default void setInt(int val, int val2){

    }
    default void setItemStack(int val, ItemStack val2){

    }
    default void setLocation(int val, Location val2){

    }
    default void setObject(int val,Object val2){

    }
    default void setString(int val, String val2){

    }


}
