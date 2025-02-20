package me.matl114.logitech.Utils.UtilClass.ItemClass;

import me.matl114.matlib.Utils.Inventory.ItemStacks.CleanItemStack;
import org.bukkit.inventory.ItemStack;

public class DisplayItemStack extends CleanItemStack implements AbstractItemStack{
    public DisplayItemStack(ItemStack is){
        super(is );
    }
    public DisplayItemStack copy(){
        return new DisplayItemStack(this);
    }
}
