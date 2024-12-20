package me.matl114.logitech.SlimefunItem.Cargo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.matl114.logitech.SlimefunItem.DistinctiveCustomItemStack;
import me.matl114.logitech.SlimefunItem.Cargo.Config.CargoConfigCard;
import me.matl114.logitech.SlimefunItem.Interface.DirectionalBlock;
import me.matl114.logitech.SlimefunItem.Interface.MenuBlock;
import me.matl114.logitech.SlimefunItem.Interface.RecipeDisplay;
import me.matl114.logitech.Utils.AddUtils;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.Settings;
import me.matl114.logitech.Utils.UtilClass.CargoClass.CargoConfigs;
import me.matl114.logitech.Utils.UtilClass.TickerClass.Ticking;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public abstract class AbstractCargo extends DistinctiveCustomItemStack implements RecipeDisplay , MenuBlock, Ticking, DirectionalBlock {
    public int defaultConfigCode=CargoConfigs.getDefaultConfig();
    protected final ItemStack[] DIRECTION_ITEM=new ItemStack[]{
            new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 无"),
            AddUtils.addGlow( new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 向北")),
            AddUtils.addGlow( new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 向西")),
            AddUtils.addGlow( new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 向南")),
            AddUtils.addGlow( new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 向东")),
            AddUtils.addGlow( new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 向上")),
            AddUtils.addGlow( new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&6点击切换方向","&3当前方向: 向下"))
    };
    public AbstractCargo(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, List<ItemStack> displayList) {
        super(itemGroup, item, recipeType, recipe,null);
        this.displayedMemory=new ArrayList<>();
        if(displayList==null){
            displayList=new ArrayList<>();
        }
        int len=displayList.size();
        for(int i=0;i<len;i++){
            displayedMemory.add(displayList.get(i));
            displayedMemory.add(null);
        }
    }
    public void cargoTask(Block b, BlockMenu menu, SlimefunBlockData data, int configCode){
        //doing nothing
    }
    public boolean conditionHandle(Block b,BlockMenu menu){
        return true;
    }
    @Override
	public abstract void constructMenu(BlockMenuPreset preset);
    public abstract int[] getBWListSlot();
    public int getConfigCode(SlimefunBlockData data){
        return DataCache.getCustomData(data,"config",-1);
    }
    public abstract int getConfigSlot();
    public int getDefaultConfig(){
        return defaultConfigCode;
    }
    @Override
	public abstract int[] getInputSlots();
    @Override
	public abstract int[] getOutputSlots();
    public void loadConfig(BlockMenu inv,Block b){
        loadConfig(inv,b,DataCache.safeLoadBlock(inv.getLocation()));
    }
    public void loadConfig(BlockMenu inv,Block b,SlimefunBlockData data){
        int code=-1;
        ItemStack item=inv.getItemInSlot(getConfigSlot());
        if(item!=null){
            ItemMeta meta=item.getItemMeta();
            if(meta!=null&& CargoConfigCard.isConfig(meta)){
                code=CargoConfigCard.getConfig(meta);
            }
        }
        DataCache.setCustomData(data,"config",code);
    }
    @Override
	public void newMenuInstance(BlockMenu menu, Block b){
        menu.addMenuOpeningHandler(player -> {
            updateMenu(menu,b,Settings.RUN);
        });
        menu.addMenuCloseHandler(player -> {
            updateMenu(menu,b,Settings.RUN);
        });
        updateMenu(menu,b, Settings.INIT);
    }
    @Override
	public void onBreak(BlockBreakEvent e, BlockMenu menu){
        MenuBlock.super.onBreak(e,menu);
        if(menu!=null){
            Location loc=menu.getLocation();
            menu.dropItems(loc,getConfigSlot());
            menu.dropItems(loc,getBWListSlot());
        }
    }
    @Override
	public void preRegister(){
        super.preRegister();
        registerTick(this);
        registerBlockMenu(this);
        handleBlock(this);
    }
    @Override
	public void tick(Block b, @Nullable BlockMenu menu, SlimefunBlockData data, int tickCount){
        if(menu.hasViewer()){
            updateMenu(menu,b,Settings.RUN);
        }
        int configCode=getConfigCode(data);
        if(conditionHandle(b,menu)&&configCode>=0){
            cargoTask(b,menu,data,configCode);
        }
    }

    public void updateMenu(BlockMenu blockMenu, Block block, Settings mod){
        loadConfig(blockMenu,block);
    }
}
