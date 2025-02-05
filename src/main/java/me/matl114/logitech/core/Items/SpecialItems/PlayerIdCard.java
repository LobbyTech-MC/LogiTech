package me.matl114.logitech.core.Items.SpecialItems;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.matl114.logitech.Utils.AddUtils;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.WorldUtils;
import me.matl114.logitech.core.Cargo.Links.HyperLink;
import me.matl114.logitech.core.DistinctiveCustomSlimefunItem;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerIdCard extends DistinctiveCustomSlimefunItem {
    public PlayerIdCard(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, List<ItemStack> displayInfo) {
        super(itemGroup, item, recipeType, recipe, displayInfo);
    }
    @Override
    public void preRegister(){
        super.preRegister();
        addItemHandler((ItemUseHandler)this::onBindPlayer);
    }
    private final NamespacedKey KEY_UID = AddUtils.getNameKey("plaid");
    public final static String UID_DISPLAY_PREFIX = AddUtils.resolveColor("&x&E&B&3&3&E&B绑定的玩家: &f");
    public UUID getUid(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta!=null){
            try{
                PersistentDataContainer container = meta.getPersistentDataContainer();
                return UUID.fromString(container.get(KEY_UID, PersistentDataType.STRING));
            }catch(Throwable ex){}
        }
        return null;
    }
    private void onBindPlayer(PlayerRightClickEvent event) {
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            if(event.getPlayer().isSneaking()){
                if(dataContainer.has(KEY_UID, PersistentDataType.STRING)){
                    dataContainer.remove(KEY_UID);
                    var lores = meta.lore();
                    if(lores!=null&&!lores.isEmpty()){
                        lores.remove(lores.size()-1);
                        meta.lore(lores);
                    }
                    AddUtils.sendMessage(event.getPlayer(), "&7[&6玩家ID卡&7] &a成功清除记录的玩家信息");
                }
            }else{
                dataContainer.set(KEY_UID,PersistentDataType.STRING,event.getPlayer().getUniqueId().toString());
                var lores = meta.getLore();
                lores = lores==null?new ArrayList<String>():lores;
                lores.add(UID_DISPLAY_PREFIX+event.getPlayer().getUniqueId().toString());
                meta.setLore(lores);
                AddUtils.sendMessage(event.getPlayer(), "&7[&6玩家ID卡&7] &a成功绑定当前玩家信息");
            }
        }

    }
}
