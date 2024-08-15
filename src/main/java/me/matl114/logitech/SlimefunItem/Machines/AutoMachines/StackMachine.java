package me.matl114.logitech.SlimefunItem.Machines.AutoMachines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.Schedule.SchedulePostRegister;
import me.matl114.logitech.SlimefunItem.Machines.AbstractAdvancedProcessor;
import me.matl114.logitech.SlimefunItem.Machines.MultiCraftType;
import me.matl114.logitech.Utils.*;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StackMachine extends AbstractAdvancedProcessor implements MultiCraftType {
    protected final int[] BORDER={
            12,14,21,23,30,31,32,39,41,48,49,50
    };
    protected final int[] INPUT_SLOT={
            0,1,2,9,10,11,18,19,20,27,28,29,36,37,38,45,46,47
    };
    protected final int[] OUTPUT_SLOT={
            6,7,8,15,16,17,24,25,26,33,34,35,42,43,44,51,52,53
    };
    public int[] getInputSlots(){
        return INPUT_SLOT;
    }
    public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }
    protected static List<SlimefunItem> BW_LIST;
    protected static int[] BW_LIST_ENERGYCOMSUME;
    protected static int BWSIZE;
    protected final int MACHINE_SLOT=13;
    protected final int MINFO_SLOT=40;
    protected final int INFO_SLOT=4;
    protected final ItemStack INFO_ITEM=new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"&b机制",
            "&6将要模拟的机器放在下方槽位","&6机器会进行模拟,其中","&7<并行处理数>=<机器数*工作效率>","&7<耗电数>=<机器数量*单个机器耗电/工作效率>","&6有关高级机器和并行处理数的信息,请见粘液书<版本与说明>","&6支持的机器可以在粘液书<通用机器类型大全>"
            ,"&6或者左边按钮查看","&6机器支持的配方可以点击右侧按钮查看");
    protected final ItemStack MINFO_ITEM_OFF=new CustomItemStack(Material.RED_STAINED_GLASS_PANE,"&c机器信息","&7待机中");
    protected final int MACHINEMENU_SLOT=3;
    protected final int RECIPEMENU_SLOT=5;
    protected final ItemStack MACHINEMENU_ICON=new CustomItemStack(Material.BLAST_FURNACE,"&b该机器支持的机器列表","&6点击打开菜单");
    protected final ItemStack RECIPEMENU_ICON=new CustomItemStack(Material.KNOWLEDGE_BOOK,"&b当前模拟的机器配方列表","&6点击打开菜单");
    protected ItemStack getInfoItem(int craftlimit,int energyCost,int charge,double efficiency,String name){
        return new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,"&a机器信息","&7当前模拟的机器名称: %s".formatted(name),
                "&7当前并行处理数: %-3d".formatted(craftlimit),"&7当前每刻耗电量: %sJ/t".formatted(AddUtils.formatDouble(energyCost)),"&7当前电量: %sJ".formatted(AddUtils.formatDouble(charge)),
                "&7当前工作效率: %s".formatted(AddUtils.getPercentFormat(efficiency)));
    }
    protected ItemStack getInfoOffItem(int craftlimit ,int energyCost,int charge,String name){
        return new CustomItemStack(Material.RED_STAINED_GLASS_PANE,"&c机器信息","&c缺少电力!","&7当前模拟的机器名称: %s".formatted(name),
                "&7当前并行处理数: %-3d".formatted(craftlimit),"&7当前每刻耗电量: %sJ/t".formatted(AddUtils.formatDouble(energyCost)),"&7当前电量: %sJ".formatted(AddUtils.formatDouble(charge)));
    }
    protected double efficiency;
    static{
        SchedulePostRegister.addPostRegisterTask(()->{
            getMachineList();
        });
    }
    public StackMachine(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                        Material progressItem, int energyConsumption, int energyBuffer,double efficiency) {
        super(category, item, recipeType, recipe, progressItem, energyConsumption, energyBuffer, null);
        AddUtils.addGlow(getProgressBar());
        this.efficiency=efficiency;

    }
    public void addInfo(ItemStack stack){

    }
    public static List<SlimefunItem> getMachineList(){
        if(BW_LIST==null){
            RecipeSupporter.init();
            BWSIZE=RecipeSupporter.STACKMACHINE_LIST.size();
            BW_LIST=new ArrayList<>(BWSIZE+1);
            BW_LIST_ENERGYCOMSUME=new int[BWSIZE];
            int i=0;
            for(Map.Entry<SlimefunItem,Integer> e:RecipeSupporter.STACKMACHINE_LIST.entrySet()){
                BW_LIST.add(e.getKey());
                BW_LIST_ENERGYCOMSUME[i]=e.getValue();
                ++i;
            }
        }
        return BW_LIST;
    }
    public static final int getListSize(){
        if(BW_LIST==null||BWSIZE==0){
            BWSIZE=getMachineList().size();
        }
        return BWSIZE;
    }
    public static int getEnergy(int index){
        if(BW_LIST_ENERGYCOMSUME==null||BW_LIST_ENERGYCOMSUME.length==0){
            getMachineList();
        }
        return BW_LIST_ENERGYCOMSUME[index];
    }
    public ItemStack getProgressBar() {
        return progressbar;
    }
    public MachineRecipe getRecordRecipe(Location loc){
        List<MachineRecipe> lst=getMachineRecipes(DataCache.safeLoadBlock(loc));
        int size=lst.size();
        if(size>0){
            int index=DataCache.getLastRecipe(loc);
            if(index>=0&&index<size){
                return lst.get(index);
            }
        }
        return null;
    }
    public MachineRecipe getRecordRecipe(SlimefunBlockData data){
        List<MachineRecipe> lst=getMachineRecipes(data);
        int size=lst.size();
        if(size>0){
            int index=DataCache.getLastRecipe(data);
            if(index>=0&&index<size){
                return lst.get(index);
            }
        }
        return null;
    }
    public List<MachineRecipe> getMachineRecipes() {
        return new ArrayList<>();
    }
    public List<MachineRecipe> getMachineRecipes(Block b, BlockMenu inv){
        return getMachineRecipes(DataCache.safeLoadBlock(inv.getLocation()));
    }
    public List<MachineRecipe> getMachineRecipes(SlimefunBlockData data){
        int index= MultiCraftType.getRecipeTypeIndex(data);
        if(index>=0&&index<getListSize()){
            List<MachineRecipe> lst= RecipeSupporter.MACHINE_RECIPELIST.get(getMachineList().get(index ));
            return lst!=null?lst:new ArrayList<>();
        }
        return new ArrayList<>();
    }
    //该方法一般只有updateMenu的时候调用
    //有没有可能就是
    //老子直接不要了塞updateMenu里
    public void anaylsisMachine(BlockMenu inv){
        //首先 检测机器
        //其次 核验下标 使用safeset 需要重置历史吗 随便 但是你禁用更好
        //如果禁用

    }
    public void constructMenu(BlockMenuPreset preset) {
        //空白背景 禁止点击
        int[] border = BORDER;
        int len=border.length;
        for(int var4 = 0; var4 < len; ++var4) {
            preset.addItem(border[var4], ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        preset.addItem(MACHINEMENU_SLOT,MACHINEMENU_ICON);
        preset.addItem(INFO_SLOT,INFO_ITEM,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(RECIPEMENU_SLOT,RECIPEMENU_ICON);
        preset.addItem(MINFO_SLOT,MINFO_ITEM_OFF,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(PROCESSOR_SLOT, MenuUtils.PROCESSOR_NULL, ChestMenuUtils.getEmptyClickHandler());
    }
    public void newMenuInstance(BlockMenu inv, Block block){
        inv.addMenuOpeningHandler((player -> {
            updateMenu(inv,block,Settings.RUN);
        }));
        inv.addMenuCloseHandler((player -> {
            updateMenu(inv,block,Settings.RUN);
        }));
        inv.addMenuClickHandler(RECIPEMENU_SLOT,((player, i, itemStack, clickAction) -> {
            int index=MultiCraftType.getRecipeTypeIndex(inv.getLocation());
            if(index>=0&&index<getListSize()){
                SlimefunItem item=getMachineList().get(index);

                MenuUtils.createMRecipeListDisplay(item.getItem(),RecipeSupporter.MACHINE_RECIPELIST.get(item),
                        ((player1, i1, itemStack1, clickAction1) -> {
                            inv.open(player1);
                            return false;
                        })
                ).open(player);
            }else{
                player.sendMessage(ChatColors.color("&e您所放置的机器为空或者为不支持的机器"));
                // player.sendMessage();
            }
            return false;
        }));
        inv.addMenuClickHandler(MACHINEMENU_SLOT,((player, i, itemStack, clickAction) -> {
            MenuUtils.createMachineListDisplay(getMachineList(),((player1, i1, itemStack1, clickAction1) -> {
                inv.open(player1);
                return false;
            })).open(player);
            return false;
        }));

        updateMenu(inv,block,Settings.INIT);
    }
    public int getCraftLimit(Block b,BlockMenu inv){
       return inv.getItemInSlot(MACHINE_SLOT).getAmount();
    }
    public void updateMenu(BlockMenu inv, Block block, Settings mod){
//        if(mod==Settings.INIT){
//
//        }
        SlimefunBlockData data=DataCache.safeLoadBlock(inv.getLocation());
        ItemStack it=inv.getItemInSlot(MACHINE_SLOT);
        SlimefunItem sfitem=SlimefunItem.getByItem(it);
        int index=MultiCraftType.getRecipeTypeIndex(data);
        if(sfitem!=null){
            SlimefunItem historyMachine;
            if(index>=0&&index<getListSize()){
                historyMachine= getMachineList().get(index);
            }else {
                historyMachine=null;
            }
            if(sfitem==historyMachine){
                //和历史机器一样
                return;
            }else {
                int size=getListSize();
                List<SlimefunItem> lst=getMachineList();
                for(int i=0;i<size;++i){
                    if(sfitem==lst.get(i)){
                        //是该机器,设置下标，都不用查了 肯定不一样
                        MultiCraftType.forceSetRecipeTypeIndex(data,i);
                        int charge=getEnergy(i);
                        DataCache.setCustomData(data,"mae",charge==0?energyConsumption:charge);
                        return;
                    }
                }
            }//找不到,给你机会你不重用啊
        }
        if(index!=-1)//即将变成-1,不一样才变,不用重复查询
            MultiCraftType.forceSetRecipeTypeIndex(data,-1);
            DataCache.setCustomData(data,"mae",0);
    }
    public void processorCost(Block b,BlockMenu inv){
        Location loc=inv.getLocation();
        int charge=DataCache.getCustomData(loc,"mae",energyConsumption);
        int craftLimit=getCraftLimit(b,inv);
        int consumption=(int)(Math.min((craftLimit*charge)/efficiency,this.energybuffer));
        this.removeCharge(loc,consumption);
    }
    public void tick(Block b, @Nullable BlockMenu inv, SlimefunBlockData data, int tickCount){
        //首先 加载
        long s=System.nanoTime();
        if(inv.hasViewer()){
            updateMenu(inv,b,Settings.RUN);
        }
        int index=MultiCraftType.getRecipeTypeIndex(data);
        if(index>=0&&index<getListSize()){//有效机器
            int charge=DataCache.getCustomData(data,"mae",energyConsumption);
            int craftLimit=getCraftLimit(b,inv);
            int consumption=(int)(Math.min((craftLimit*charge)/efficiency,this.energybuffer));
            int energy=this.getCharge(inv.getLocation(),data);
            if(energy>consumption){
                if(inv.hasViewer()){
                    inv.replaceExistingItem(MINFO_SLOT,getInfoItem(craftLimit,consumption,energy,this.efficiency,ItemStackHelper.getDisplayName(inv.getItemInSlot(MACHINE_SLOT))));
                }
                long a=System.nanoTime();
                process(b,inv,data);
                long end=System.nanoTime();
                Debug.logger("pre load time cost ",(a-s)," ns");
                Debug.logger("process time cost "+(end-a)+" ns");
            }else {
                //没电
                if(inv.hasViewer()){
                    inv.replaceExistingItem(MINFO_SLOT,getInfoOffItem(craftLimit,consumption,energy,ItemStackHelper.getDisplayName(inv.getItemInSlot(MACHINE_SLOT))));
                }
            }

        }else {
            if(inv.hasViewer()){
                inv.replaceExistingItem(MINFO_SLOT,MINFO_ITEM_OFF);
            }
        }
    }

}