package me.matl114.logitech.SlimefunItem.Cargo.Config;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import me.matl114.logitech.Utils.DataCache;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public interface ChipControllable {
    static final String CCODEKEY="ccmd";
    public  int getChipSlot();
    default String getKey(){
        return CCODEKEY;
    }
    default void loadChipCommand(BlockMenu inv){
        SlimefunBlockData data= DataCache.safeLoadBlock(inv.getLocation());
        ItemStack it=inv.getItemInSlot(getChipSlot());
        if(it!=null){
            ItemMeta im=it.getItemMeta();
            if(ChipCardCode.isConfig(im)){
                int code=ChipCardCode.getConfig(im);
                data.setData(CCODEKEY,String.valueOf(code));
                return;
            }
        }
        data.setData(CCODEKEY,"n");
        return;
    }
}
