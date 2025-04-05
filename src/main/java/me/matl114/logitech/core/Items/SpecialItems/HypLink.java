package me.matl114.logitech.core.Items.SpecialItems;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.matl114.logitech.core.Cargo.Links.HyperLink;
import me.matl114.logitech.core.Items.Abstracts.DataRecordedItem;
import me.matl114.logitech.listeners.Events.InteractPermissionTestEvent;
import me.matl114.logitech.listeners.Listeners.HyperLinkListener;
import me.matl114.logitech.utils.AddUtils;
import me.matl114.logitech.utils.DataCache;
import me.matl114.logitech.utils.WorldUtils;
import me.matl114.matlib.utils.inventory.inventoryRecords.InventoryRecord;
import me.matl114.matlib.utils.inventory.inventoryRecords.SimpleInventoryRecord;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Optional;

public class HypLink extends DataRecordedItem {
    public HypLink(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe,new ArrayList<>());
    }
    @Override
    public void preRegister(){
        super.preRegister();
        addItemHandler((ItemUseHandler)this::onBindLocation);
    }
    private void onBindLocation(PlayerRightClickEvent event) {
        if(event.getPlayer().isSneaking()){
            Optional<Block> b = event.getClickedBlock();
            if(b.isPresent()){
                ItemStack it= event.getItem();
                ItemMeta im = it.getItemMeta();
                if(HyperLink.canLink(im)){
                    if(WorldUtils.hasPermission(
                        event.getPlayer(),b.get(),Interaction.INTERACT_BLOCK, Interaction.BREAK_BLOCK, Interaction.PLACE_BLOCK)){
                        HyperLink.setLink(im,b.get().getLocation());
                        it.setItemMeta(im);
                    }else {
                        AddUtils.sendMessage(event.getPlayer(),"&c抱歉,但您似乎并不能在该位置使用此物品.");
                    }
                }
            }
        }else{
            //try load
            ItemStack it= event.getItem();
            ItemMeta im = it.getItemMeta();
            if(HyperLink.isLink(im)){
                Location loc=HyperLink.getLink(im);
                if(loc!=null) {
                    if (WorldUtils.hasPermission(event.getPlayer(), loc, Interaction.INTERACT_BLOCK, Interaction.BREAK_BLOCK, Interaction.PLACE_BLOCK)) {
                        SlimefunBlockData data = DataCache.safeLoadBlock(loc);
                        if (data != null) {
                            SlimefunItem item = SlimefunItem.getById( data.getSfId());
                            BlockMenu menu = data.getBlockMenu();
                            if (menu != null && item != null ){
                                if(item.canUse(event.getPlayer(), true) && menu.canOpen(loc.getBlock(), event.getPlayer()))
                                    menu.open(event.getPlayer());
                                else
                                    AddUtils.sendMessage(event.getPlayer(),"&c打开该粘液方块的行为被阻止!");
                                return;
                            }
                        }
                        Block b = loc.getBlock();
                        if(me.matl114.matlib.utils.WorldUtils.isInventoryHolder(b.getType())){
                            PlayerInteractEvent interactTarget=new InteractPermissionTestEvent(event.getPlayer(), Action.RIGHT_CLICK_BLOCK,null,b, BlockFace.UP);
                            try{
                                Bukkit.getPluginManager().callEvent(interactTarget);
                                if(interactTarget.isCancelled()){
                                    AddUtils.sendMessage(event.getPlayer(), "&c点击该方块的行为被阻止!");
                                    return;
                                }
                            }catch (Throwable ignored){
                                AddUtils.sendMessage(event.getPlayer(), "&c点击该方块时出现未知错误!");
                                return;
                            }
                            //create only vanilla record
                            InventoryRecord record = SimpleInventoryRecord.getInventoryRecord(loc,true);
                            //BlockState state = b.getState(false);
                            //
                            if(record.inventory()!=null && record.stillValid()){
                                //todo do test
                                HyperLinkListener.openHypInv(event.getPlayer(),record);
                            }
                        }else {
                            AddUtils.sendMessage(event.getPlayer(),"&c抱歉,但超链接所指向的方块似乎并不是一个有效的容器");
                        }


                    }else {
                        AddUtils.sendMessage(event.getPlayer(), "&c抱歉,但您似乎并没有访问该位置的权限.");
                    }
                }
            }
        }
    }
}
