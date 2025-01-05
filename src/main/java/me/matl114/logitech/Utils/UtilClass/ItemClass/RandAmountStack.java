package me.matl114.logitech.Utils.UtilClass.ItemClass;

import me.matl114.logitech.Utils.AddUtils;
import me.matl114.matlib.Utils.Inventory.CleanItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RandAmountStack extends CleanItemStack implements AbstractItemStack,RandOutItem{
    public Random rand=new Random();
    private int min;
    private int len;
    public RandAmountStack(ItemStack stack,int min,int max) {
        super(stack);
        this.min = min;
        this.len = max-min+1;

    }
    @Override
	public ItemStack clone(){
        ItemStack clone =super.clone();
        clone.setAmount(min+ rand.nextInt(len));
        return clone;
    }
    @Override
	public RandAmountStack copy(){
        return new RandAmountStack(this,min,min+len-1);
    }
    @Override
	public ItemStack getInstance(){
        return this.clone();
    }
    public int getMax(){
        return len+min-1;
    }
    public int getMin() {
        return min;
    }
}
