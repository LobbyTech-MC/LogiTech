package me.matl114.logitech.Utils.UtilClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.matl114.logitech.Utils.Debug;

public class TestItemStack extends ItemStack {
    public TestItemStack(ItemStack item) {
        super(item);
    }
    public TestItemStack( Material type, int amount, short damage,  Byte data) {
        super(type, amount, damage, data);
    }
    public boolean equals(Object a){
        Debug.logger("equal called");
        Debug.logger("beyond");
        return super.equals(a);
    }
    public int hashCode(){
        Debug.logger("hash called");
        Debug.logger("beyond");
        return super.hashCode();
    }
}
