package me.matl114.logitech.Utils.UtilClass.MenuClass;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public class MenuPreset {

    int size;
    int prelen;
    int suflen;
    HashMap<Integer, ItemStack> preitems=new HashMap<>();
    HashMap<Integer, ChestMenu.MenuClickHandler> prehandlers=new HashMap<>();
    public MenuPreset(int size){
        this(size,0,0);
    }
    public MenuPreset( int size, int prelen, int suflen) {

        this.size = size;
        this.prelen = prelen;
        this.suflen = suflen;
    }

    public MenuPreset addItem(ChestMenu.MenuClickHandler handler,int... slot) {
        for(int slot_i :slot) {
			prehandlers.put(slot_i, handler);
		}
        return this;
    }
    public MenuPreset addItem(ItemStack item, ChestMenu.MenuClickHandler handler,int... slot) {
        for(int slot_i :slot) {
			prehandlers.put(slot_i, handler);
		}
        return addItem(item,slot);
    }
    public MenuPreset addItem( ItemStack item,int... slot) {
        for(int slot_i :slot) {
			preitems.put(slot_i, item);
		}
        return this;
    }
    public HashMap<Integer, ChestMenu.MenuClickHandler> getPrehandlers() {
        return prehandlers;
    }
    public HashMap<Integer, ItemStack> getPreitems() {
        return preitems;
    }

    public int getPrelen() {
        return prelen;
    }
    public int getSize() {
        return size;
    }
    public int getSuflen() {
        return suflen;
    }
    public MenuPreset setSize(int size){
        this.size = size;
        return this;
    }
}
