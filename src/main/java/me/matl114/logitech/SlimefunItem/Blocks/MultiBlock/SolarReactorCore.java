package me.matl114.logitech.SlimefunItem.Blocks.MultiBlock;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.MyAddon;
import me.matl114.logitech.Schedule.PersistentEffects.CustomEffects;
import me.matl114.logitech.Schedule.PersistentEffects.PlayerEffects;
import me.matl114.logitech.Schedule.ScheduleEffects;
import me.matl114.logitech.Schedule.ScheduleSave;
import me.matl114.logitech.Schedule.Schedules;
import me.matl114.logitech.SlimefunItem.AddItem;
import me.matl114.logitech.SlimefunItem.Machines.AbstractMachine;
import me.matl114.logitech.SlimefunItem.Machines.MenuBlock;
import me.matl114.logitech.Utils.*;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockType;
import me.matl114.logitech.Utils.UtilClass.PdcClass.AbstractLocation;
import me.matl114.logitech.Utils.UtilClass.RecipeClass.SimpleCraftingOperation;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import org.bukkit.block.Block;


import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.BlockVector;

import java.util.*;

public class SolarReactorCore extends MultiBlockProcessor {
    protected final int[] BORDER=new int[]{1,2,3,5,6,7,13,40,49};
    protected final int TOGGLE_SLOT=4;
    protected final int HOLOGRAM_SLOT=8;
    protected final ItemStack[] TOGGLE_ITEM=new ItemStack[]{
            new CustomItemStack(Material.RED_STAINED_GLASS_PANE,"&6点击切换反应堆状态",
            "&6shift点击切换是否自动构建","&7当前状态: &c关闭","&7自动构建: &c关","&7警告:你最好别在进程中关闭反应堆")
            ,new CustomItemStack(Material.RED_STAINED_GLASS_PANE,"&6点击切换反应堆状态",
            "&6shift点击切换是否自动构建","&7当前状态: &c关闭","&7自动构建: &a开","&7警告:你最好别在进程中关闭反应堆"),
            new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,"&6点击切换反应堆状态",
                    "&6shift点击切换是否自动构建","&7当前状态: &a开启","&7自动构建: &c关","&7警告:你最好别在进程中关闭反应堆")
            ,new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,"&6点击切换反应堆状态",
            "&6shift点击切换是否自动构建","&7当前状态: &a开启","&7自动构建: &a开","&7警告:你最好别在进程中关闭反应堆")
    };
    protected final ItemStack HOLOGRAM_ITEM_ON=new CustomItemStack(SlimefunItems.HOLOGRAM_PROJECTOR,"&6点击切换全息投影","&7当前状态: &a开启");
    protected final ItemStack HOLOGRAM_ITEM_OFF=new CustomItemStack(SlimefunItems.HOLOGRAM_PROJECTOR,"&6点击切换全息投影","&7当前状态: &c关闭");
    protected final int HALT_SLOT=0;
    protected final ItemStack HALT_ITEM=new CustomItemStack(Material.BARRIER,"&c点击安全终止进程","&6不会引发爆炸");
    protected final int INFO_SLOT=22;
    protected final ItemStack INFO_ITEM=new CustomItemStack(Material.LIME_STAINED_GLASS_PANE,"&a机器状态信息","");
    protected final int[] INPUT_SLOT=new int[]{19,20,21,28,29,30,37,38,39};
    protected final int[] OUTPUT_SLOT=new int[]{23,24,25,32,33,34,41,42,43};
    public int[] getInputSlots(){
        return INPUT_SLOT;
    }
    public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }
    protected final double EFFECT_OFFSET=1;
    protected final double REMOVE_EFFECT_OFFSET =2;
    protected final double HOLOGRAM_OFFSET=0.5;
    protected final double REMOVE_HOLOGRAM_OFFSET=1;
    protected final double ENTITY_KILL_OFFSET=2.5;
    protected final double ENTITY_EFFECT_OFFSET=20;
    protected final int[] BORDER_IN = new int[]{9, 10, 11, 12, 18,  27, 36,45,46,47,48};
    protected final int[] BORDER_OUT = new int[]{14, 15, 16, 17, 26,35,44,50,51,52,53};
    public SolarReactorCore(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                      ItemStack[] recipe, String blockId, MultiBlockType type, int energyConsumption, int energyBuffer,
                            LinkedHashMap<Object, Integer> customRecipes){
        super(itemGroup, item, recipeType, recipe, blockId,type,AddUtils.addGlow(new ItemStack(Material.FIRE_CHARGE)),
                energyConsumption,energyBuffer,customRecipes);
        ScheduleSave.addFinalTask(()->{
            Set<Location>  locs=new HashSet<>();
            for(Location loc :EFFECT_CACHE.keySet()){
                locs.add(loc);
            }
            Iterator<Location> iter=locs.iterator();
            while(iter.hasNext()){
                Location loc=iter.next();
                removeEffect(loc);
            }
        });
        this.PROCESSOR_SLOT=31;
    }

    public void onMultiBlockDisable(Location loc,AbstractMultiBlockHandler handler){
        removeEffect(loc);
        SimpleCraftingOperation op=SolarReactorCore.this.processor.getOperation(loc);

        if(op!=null&&(!op.isFinished())){//如果还在进行中就暂停

            onDestroyEffect(loc,handler);
        }
        super.onMultiBlockDisable(loc,handler);
    }
    public void onMultiBlockEnable(Location loc,AbstractMultiBlockHandler handler){
        super.onMultiBlockEnable(loc,handler);
        removeEffect(loc);
        createEffect(loc);
    }
    public HashMap<Location,EnderCrystal> EFFECT_CACHE =new HashMap<>();
    public NamespacedKey BIND_NS=AddUtils.getNameKey("bindloc");
    public void getAsEffect(Entity entity,Location loc){
        if(entity instanceof EnderCrystal enderCrystal){
            enderCrystal.setBeamTarget(loc.clone().add(0,3,0));
            enderCrystal.setShowingBottom(false);

            enderCrystal.setInvulnerable(true);
            enderCrystal.setSilent(false);

            enderCrystal.setGravity(false);
            PersistentDataContainer container = enderCrystal.getPersistentDataContainer();
            container.set(BIND_NS, AbstractLocation.TYPE,loc);
            EFFECT_CACHE.put(loc,enderCrystal);
        }
    }
    public void createEffect(Location loc){
        Schedules.launchSchedules(Schedules.getRunnable(()->{
            Location loc2=loc.clone().add(0.5,1,0.5);
            EnderCrystal entity=(EnderCrystal) loc.getWorld().spawnEntity(loc2, EntityType.ENDER_CRYSTAL);
            getAsEffect(entity,loc);
        }),0,true,0);
    }
    public void removeEffect(Location loc){
        EnderCrystal cry= EFFECT_CACHE.remove(loc);
        Location loc2=loc.clone().add(0.5,1,0.5);
        Schedules.launchSchedules(Schedules.getRunnable(()->{
            if(cry!=null)
                cry.remove();
            Collection<Entity> allCrystal=loc2.getWorld().getNearbyEntities(loc2, REMOVE_EFFECT_OFFSET, REMOVE_EFFECT_OFFSET, REMOVE_EFFECT_OFFSET,(entity -> {
                return entity.getType()==EntityType.ENDER_CRYSTAL&&checkBind(entity,loc);
            }));
            for (Entity entity:allCrystal){
                entity.remove();
            }
        }),0,true,0);
    }
    public boolean checkBind(Entity entity,Location loc){
        if(entity instanceof EnderCrystal) {
            PersistentDataContainer container=entity.getPersistentDataContainer();
            Location loc2=container.get(BIND_NS,AbstractLocation.TYPE);
            if(loc.equals(loc2)){

                return true;
            }
        }
        return false;
    }
//    public class SolarDamage implements DamageType {
//
//        public SolarDamage(){
//
//        }
//        NamespacedKey key=AddUtils.getNameKey("solar");
//        public String getTranslationKey(){
//            return  "solar_kill";
//        }
//        public DamageScaling getDamageScaling(){
//            return DamageScaling.NEVER;
//        }
//
//
//        public DamageEffect getDamageEffect(){
//            return DamageEffect.HURT;
//        }
//
//
//        public DeathMessageType getDeathMessageType(){
//            return DeathMessageType.DEFAULT;
//        }
//
//        public float getExhaustion(){
//            return 1.0f;
//        }
//        public NamespacedKey getKey(){
//            return key;
//        }
//    }
//    public SolarDamage DAMAGETYPE=new SolarDamage();
    public BlockVector[] checkedLocation=new ArrayList<BlockVector>(){{
    for(int i=-1;i<=1;++i){
        for(int j=-1;j<=1;++j){
            for(int k=-1;k<=1;++k){
                if(i!=0||j!=0||k!=0){
                    add(new BlockVector(i,j,k));
                }
            }
        }
    }
    }}.toArray(BlockVector[]::new);
    public boolean checkCondition(Location loc){
        //no endcrystal
        EnderCrystal cry= EFFECT_CACHE.get(loc);
        if(cry!=null&&cry.isValid()){
            Location loc2=cry.getLocation();
            Location loc1=loc.clone().add(0.5,1,0.5);
            if(loc2.distance(loc1)>EFFECT_OFFSET){
                return false;
            }
        }else {
            return false;
        }
        // has block
        boolean hasblock=false;
        int len=checkedLocation.length;
        for(int i=0;i<4;++i){//26中选3个
            int locIndex=AddUtils.random(len);
            Location loc1=loc.clone().add(checkedLocation[locIndex]);
            Block block=loc1.getBlock();
            if(!block.getType().isAir()){
                hasblock=true;
                for(int s=0;s<len;++s){
                    loc1=loc.clone().add(checkedLocation[s]);
                    block=loc1.getBlock();
                    WorldUtils.removeSlimefunBlock(loc1,true);
                    block.setType(Material.AIR);
                }
                break;
            }
        }

        if (hasblock){
            return false;
        }
        //kill all nearby entity
        Schedules.launchSchedules(Schedules.getRunnable(()->{
            Location loc1=loc.clone().add(0.5,1,0.5);
            Collection<Entity> allEntities=loc1.getWorld().getNearbyEntities(loc1,ENTITY_KILL_OFFSET,ENTITY_KILL_OFFSET,ENTITY_KILL_OFFSET);
            for(Entity entity:allEntities){
                if(entity instanceof EnderCrystal){

                }else if(entity instanceof Player player){
                    PlayerEffects.grantEffect(CustomEffects.SOLAR_BURN,player,2,3,(player1 -> {return loc1.distance(player1.getLocation())<ENTITY_KILL_OFFSET+1;}));// DamageSource.builder(DAMAGETYPE).build());
                }else{
                    entity.remove();
                }
            }
        }),0,true,0);
        return true;
    }
    public void onDestroyEffect(Location loc,AbstractMultiBlockHandler handler){
        removeEffect(loc);

        AddUtils.broadCast("&e位于[%s,%.0f,%.0f,%.0f]的超新星模拟器即将爆炸,快跑!".formatted(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ()));
        BlockMenu inv=StorageCacheUtils.getMenu(loc);
        if(inv!=null){
            inv.close();
        }
        loc.getWorld().createExplosion(loc,32,true,true);
        Location center=loc.clone();
        Schedules.launchSchedules(()->{
            Collection<Entity> entities=center.getWorld().getNearbyEntities(center,ENTITY_EFFECT_OFFSET,ENTITY_EFFECT_OFFSET,ENTITY_EFFECT_OFFSET);
            for(Entity entity:entities){
                if(entity instanceof Player player){
                    PlayerEffects.grantEffect(CustomEffects.SOLAR_BURN,player,1,20);
                }
            }
        },0,true,0);
        Schedules.launchSchedules(Schedules.getRunnable(()->{
            //延后特效
            //防止重新构建
            MultiBlockService.deleteMultiBlock(center);
            //清除方块
            WorldUtils.removeSlimefunBlock(center,true);
            center.getBlock().setType(Material.CRYING_OBSIDIAN);
            int len=handler.getSize();
            for (int i=0;i<len;++i){
                int chance=AddUtils.random(3);
                if(chance==(i%3)){
                    Location loc2=handler.getBlockLoc(i);
                    loc2.getBlock().setType(Material.CRYING_OBSIDIAN);
                    WorldUtils.removeSlimefunBlock(loc2,true);
                }
            }
            len=checkedLocation.length;
            for(int i=0;i<len;++i){
                Location loc2=center.clone().add(checkedLocation[i]);
                loc2.getBlock().setType(Material.CRYING_OBSIDIAN);
                WorldUtils.removeSlimefunBlock(loc2,true);
            }
            //防止重新发起
            this.processor.endOperation(loc);
            loc.getWorld().createExplosion(center.clone().add(6,1,0),90,true,true);
            loc.getWorld().createExplosion(center.clone().add(0,1,6),90,true,true);
            loc.getWorld().createExplosion(center.clone().add(-6,1,0),90,true,true);
            loc.getWorld().createExplosion(center.clone().add(0,1,-6),90,true,true);
        }),100,true,0);
    }
    public HashMap<String,ItemStack> MBID_TO_ITEM=new HashMap<>(){{
        put("solar.frame", AddUtils.addGlow(AddItem.SOLAR_REACTOR_FRAME.clone()));
        put("solar.glass", AddUtils.addGlow(AddItem.SOLAR_REACTOR_GLASS.clone()));
    }};
    public void constructMenu(BlockMenuPreset preset){
        int[] border = BORDER;
        int len=border.length;
        for(int var4 = 0; var4 < len; ++var4) {

            preset.addItem(border[var4], ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        //输入槽边框
        border = BORDER_IN;
        len = border.length;
        for(int var4 = 0; var4 <len; ++var4) {

            preset.addItem(border[var4], ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }
        //输出槽边框
        border = BORDER_OUT;
        len = border.length;
        for(int var4 = 0; var4 <len; ++var4) {

            preset.addItem(border[var4], ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }
        //空白边框
        preset.addItem(PROCESSOR_SLOT, MenuUtils.PROCESSOR_NULL, ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(INFO_SLOT,INFO_ITEM, ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(HALT_SLOT,HALT_ITEM);
    }
    public void updateMenu(BlockMenu inv, Block block, Settings mod){
        Location loc=block.getLocation();
        int holoStatus=DataCache.getCustomData(loc,"holo",0);
        if(holoStatus==0){
            inv.replaceExistingItem(HOLOGRAM_SLOT,HOLOGRAM_ITEM_OFF);

        }else {
            inv.replaceExistingItem(HOLOGRAM_SLOT,HOLOGRAM_ITEM_ON);
        }
        int status=MultiBlockService.getStatus(loc)==0?0:1;
        int autoRec=DataCache.getCustomData(loc,"auto",0)==0?0:1;
        inv.replaceExistingItem(TOGGLE_SLOT,TOGGLE_ITEM[status*2+autoRec]);
        if(status==0){

            inv.replaceExistingItem(INFO_SLOT,INFO_ITEM);
        }

    }
    public void newMenuInstance(BlockMenu inv, Block block){
        inv.addMenuOpeningHandler((player -> {
            updateMenu(inv,block,Settings.RUN);
        }));
        inv.addMenuClickHandler(HALT_SLOT,((player, i, itemStack, clickAction) -> {
            this.processor.endOperation(block);
            inv.replaceExistingItem(PROCESSOR_SLOT,MenuUtils.PROCESSOR_NULL);
            return false;
        }));
        inv.addMenuClickHandler(TOGGLE_SLOT,((player, i, itemStack, clickAction) -> {
            Location loc=inv.getLocation();
            int statusCode=MultiBlockService.getStatus(loc);
            int autoCode=DataCache.getCustomData(loc,"auto",0)<=0?0:1;
            if(clickAction.isShiftClicked()){
                DataCache.setCustomData(loc,"auto",1-autoCode);
            }
            else if(autoCode==0){
                if(statusCode==0){
                    if(this.getCharge(loc)<energyConsumption){
                        //没电,
                        AddUtils.sendMessage(player,"&c电力不足,无法构建多方块结构!");
                    }
                    else {
                        if(MultiBlockService.createNewHandler(loc,getBuilder(),getMultiBlockType())){
                            AddUtils.sendMessage(player,"&a成功激活多方块结构!");
                        }else {
                            AddUtils.sendMessage(player,"&c多方块结构不全或者结构冲突!");
                        }
                    }
                }else {
                    MultiBlockService.deleteMultiBlock(DataCache.getLastUUID(loc));
                    AddUtils.sendMessage(player,"&a成功关闭多方块结构!");
                }
            }else{
                AddUtils.sendMessage(player,"&c多方块核心处于自动构建模式无法进行手动操作");
            }
            updateMenu(inv,block,Settings.RUN);
            return false;
        }));
        DataCache.setCustomData(inv.getLocation(),"holo",0);
        inv.addMenuClickHandler(HOLOGRAM_SLOT,((player, i, itemStack, clickAction) -> {
            Location loc=inv.getLocation();
            int holoStatus=DataCache.getCustomData(inv.getLocation(),"holo",0);
            int statusCode=MultiBlockService.getStatus(loc);
            MultiBlockService.removeHologram(loc);

            if(holoStatus==0&&statusCode==0){
                AddUtils.sendMessage(player,"&a全息投影已开启!");
                MultiBlockService.createHologram(loc,MBTYPE, MultiBlockService.Direction.NORTH, MBID_TO_ITEM);
                DataCache.setCustomData(loc,"holo",1);
            }
            updateMenu(inv,block,Settings.RUN);
            return false;
        }));
        updateMenu(inv,block,Settings.INIT);
    }
    public void tick(Block b, BlockMenu inv, SlimefunBlockData data, int tickCount){
        //in this case .blockMenu is null
        int statusCode=MultiBlockService.getStatus(data);
        Location loc=b.getLocation();
        int charge=this.getCharge(loc);
        String code=data.getData("auto");
        int autoCode=code==null?0:Integer.parseInt(code);
        if(statusCode==0){
            if(autoCode>0&&charge>2*energyConsumption){//自动构建开启 且有能量
                autoBuild(loc,data,autoCode);
            }
        }else if(MultiBlockService.acceptCoreRequest(b.getLocation(),getBuilder(),getMultiBlockType())){
            //runtime检查是否完整,每3tick检查一次,每次有1/10的概率检测一个方块
            runtimeCheck(loc,data,autoCode);
            //直接开销电量
            super.processorCost(b,inv);
            if((!checkCondition(loc)&&statusCode>0)||charge<energyConsumption){
                //避免重连的时候出现问题,重连的时候statusCode为-3到-1,但是如果没有电 直接寄
                MultiBlockService.deleteMultiBlock(DataCache.getLastUUID(loc));
                return;
            }
            if(inv.hasViewer()){
                updateMenu(inv,b,Settings.RUN);
                ItemStack it=inv.getItemInSlot(INFO_SLOT);
                if(it!=null){
                    SimpleCraftingOperation currentOperation = (SimpleCraftingOperation)this.processor.getOperation(b);
                    AddUtils.setLore(it,"&7当前机器状态: %s".formatted((currentOperation==null)?"&7待机中":"&a进程中"),
                            "&7当前电量: %dJ/%dJ".formatted(charge,energybuffer),"&7当前耗电: %dJ/t".formatted(energyConsumption));
                }
                else
                    inv.replaceExistingItem(INFO_SLOT,INFO_ITEM);

            }
            super.process(b,inv,data);
        }
    }

    public void processorCost(Block b,BlockMenu inv){
        //覆盖父类 让process中不扣电
        //转到我的ticker里扣
    }
    public void onBreak(BlockBreakEvent e,BlockMenu menu){
        if(menu!=null){
            Location l = menu.getLocation();
            menu.dropItems(l, this.getInputSlots());
            menu.dropItems(l, this.getOutputSlots());
        }
        //禁止了父类的关闭processor的操作，改为在MultiBlockBreak中关闭
        //合理性:只有多方块完整的时候才能进行processor,在破坏多方块的时候会取消processor
    }
    public void preRegister(){

        super.preRegister();
    }
}