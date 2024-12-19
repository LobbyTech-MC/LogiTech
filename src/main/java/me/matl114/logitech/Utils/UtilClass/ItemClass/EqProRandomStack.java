package me.matl114.logitech.Utils.UtilClass.ItemClass;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class EqProRandomStack extends RandomItemStack  {
    public Random rand=new Random();
    public EqProRandomStack(LinkedHashMap<ItemStack,Integer> itemSettings) {
        super(itemSettings);
    }

    public EqProRandomStack(LinkedHashSet<ItemStack> itemSettings) {
        super(new LinkedHashMap<>(){{
           itemSettings.forEach(item -> {
               this.put(item, 1);
           });
        }});
    }

    @Override
	public ItemStack clone(){
        return this.itemList[rand.nextInt(this.sum)].clone();
    }

    @Override
	public EqProRandomStack copy(){
        EqProRandomStack stack = new EqProRandomStack(itemSettings);
        return stack;

    }
    @Override
	public ItemStack getInstance(){
        ItemStack it= this.itemList[rand.nextInt(this.sum)];
        return (it instanceof RandOutItem w)?w.getInstance():it;
    }
}
