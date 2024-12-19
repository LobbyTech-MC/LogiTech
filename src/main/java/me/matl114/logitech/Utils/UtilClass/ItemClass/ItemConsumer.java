package me.matl114.logitech.Utils.UtilClass.ItemClass;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.NotImplementedException;
import me.matl114.logitech.Utils.Settings;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class ItemConsumer extends ItemCounter {
    private static ItemConsumer INSTANCE=new ItemConsumer(new ItemStack(Material.STONE));
    public static ItemConsumer get(ItemStack item) {
        ItemConsumer consumer=INSTANCE.clone();
        consumer.init(item);
        return consumer;
    }
    private List<ItemPusher> targetConsumers;
    public ItemConsumer(ItemStack item) {
        super(item);
    }
    public void addRelate(ItemPusher target){
        getTargetConsumers().add(target);
        target.dirty=true;
    }
public void clearRelated(){
        if(targetConsumers!=null){
            targetConsumers.clear();
        }
    }
    protected ItemConsumer clone(){
        return (ItemConsumer)super.clone();
    }
    /**
     * add amount(only for recipe amount match) from target ,will not modify target
     * @param other
     */
    public void consume(ItemPusher other){
        super.consume(other);
        addRelate(other);
    }

    private List<ItemPusher> getTargetConsumers(){
        if(targetConsumers==null){
            targetConsumers=new ArrayList<>(6);
        }
        return targetConsumers;
    }
    public void grab(ItemPusher target){
        super.grab(target);
      //  addRelate(target);
    }
    protected void init(ItemStack item) {
        super.init(item);
        this.targetConsumers = null;
    }
    public void push(ItemPusher target){
        target.grab(this);
       // addRelate(target);
    }

    /**
     * clear all modifi
     */
    public void syncData(){
        clearRelated();
        super.syncData();
    }
    public void syncItems(){
        if(targetConsumers==null){
            return;
        }
        int len=targetConsumers.size();
        for(int i=0;i<len;i++){
            targetConsumers.get(i).syncData();
        }
    }
    public void updateItems(BlockMenu inv ,Settings mod){
        if(targetConsumers==null){
            return;
        }
        int len=targetConsumers.size();
        for(int i=0;i<len;i++){
            targetConsumers.get(i).updateMenu(inv);
        }
    }
    //    public ItemMeta getMeta() {
//        if(item.hasItemMeta()){
//            if (meta==null){
//                //check if const item stack
//                if(item instanceof ConstItemStack cis){
//                    meta= cis.getItemMetaConst();
//                }else{
//                    meta=item.getItemMeta();
//                }
//            }
//            return meta;
//        }
//        return null;
//    }
    public void updateItemStack(){
        throw new NotImplementedException("this method should not be called");
    }

    //used as consume
}
