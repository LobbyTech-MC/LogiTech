package me.matl114.logitech.core.Machines.Abstracts;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.matl114.logitech.utils.*;
import me.matl114.logitech.utils.UtilClass.ItemClass.ItemConsumer;
import me.matl114.logitech.utils.UtilClass.ItemClass.ItemGreedyConsumer;
import me.matl114.logitech.utils.UtilClass.ItemClass.ItemPusher;
import me.matl114.logitech.utils.UtilClass.MenuClass.MenuFactory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWorkBench extends AbstractMachine {
    protected final int CRAFT_LIMIT;

    protected int getRecipeMenuSlot(){
        return -1;
    }
    protected static final ItemStack LAZY_ONECLICK=new CustomItemStack(Material.KNOWLEDGE_BOOK,"&b使用物品栏中的物品摆放配方","&7左键放入一份配方","&7右键放入64份配方","&a欢呼吧 懒狗们");
    protected static final ItemStack RECIPEBOOK_SHOW_ITEM=new CustomItemStack(Material.BOOK,"&6点击查看配方","","&a而且有一键放置配方的功能","&b欢呼吧 懒狗们");

    protected final ItemSetting<Boolean> checkRecipePermission;
    public AbstractWorkBench(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                            int energybuffer, int energyConsumption,int limit, List<Pair<Object,Integer>> shapedRecipes){
        super(category,item,recipeType,recipe,energybuffer,energyConsumption);
        this.CRAFT_LIMIT=limit;
        if(shapedRecipes != null){
            machineRecipes = new ArrayList<>(shapedRecipes.size());
            var customRecipes2 = AddUtils.buildRecipeMap(shapedRecipes);
            for(var recipePiece:customRecipes2){
                //no need to stack and can not stack(maybe some shitmachine will stack
                //but we stack it in order to format up
                this.machineRecipes.add(MachineRecipeUtils.stackFromMachine(
                        new MachineRecipe(recipePiece.getSecondValue(),recipePiece.getFirstValue().getFirstValue(), recipePiece.getFirstValue().getSecondValue())
                ));
            }
        }else{
            machineRecipes = new ArrayList<>();
        }
        checkRecipePermission = create("check-recipe-permission", false);

    }
    public void addInfo(ItemStack item){
        item.setItemMeta(AddUtils.advancedMachineShow(item,CRAFT_LIMIT).getItemMeta());
        if(this.energyConsumption > 0){
            item.setItemMeta(AddUtils.workBenchInfoAdd(item,this.energybuffer,this.energyConsumption).getItemMeta());
        }
    }
    public List<MachineRecipe> provideDisplayRecipe(){
        List<MachineRecipe> machineRecipes =  getMachineRecipes();
        List<MachineRecipe> targetRecipe = new ArrayList<>();
        int size=machineRecipes.size();
        for (int i=0;i<size;++i) {
            targetRecipe.add(MachineRecipeUtils.stackFrom(machineRecipes.get(i).getTicks(),
                    Utils.array(AddUtils.getInfoShow("&f有序配方合成","&7请在配方显示界面或者机器界面查看")),machineRecipes.get(i).getOutput()));
        }
        return targetRecipe;
    }
    /**
     * construct your menu here.called in constructor
     * @param preset
     */
    public abstract void constructMenu(BlockMenuPreset preset);
    public MenuFactory getRecipeMenu(Block b,BlockMenu inv){

         return MenuUtils.createMRecipeListDisplay(getItem(),getMachineRecipes(),null,(MenuUtils.RecipeMenuConstructor)(itemstack, recipes, backhandler,optional,history)->{
            return MenuUtils.createMRecipeDisplay(itemstack,recipes,backhandler,optional).addOverrides(8,LAZY_ONECLICK).addHandler(8,((player, i, itemStack, clickAction) -> {
                moveRecipe(player,inv,recipes, clickAction.isRightClicked());
                return false;
            }));
        });
    }
    public int getCraftLimit(Block b,BlockMenu inv){
        return CRAFT_LIMIT;
    }
    /**
     * cargo and IO
     * @return
     */
    public abstract int[] getInputSlots();

    /**
     * cargo and IO
     * @return
     */
    public abstract int[] getOutputSlots();

    public abstract int[] getRecipeSlots();
    public void registerTick(SlimefunItem item){
        //no ticker
    }
    public boolean conditionHandle(Block b,BlockMenu menu){
        //do nothing
        return true;
    }
    protected void progressorCost(Block b, BlockMenu menu) {
        //do nothing
    }
    public void craft(Block b,BlockMenu inv,Player player){
        Location loc=inv.getLocation();
        int charge=getCharge(inv.getLocation());
        int limit;
        if(this.energyConsumption > 0){
            int chargelimit=(charge/this.energyConsumption);
            if(chargelimit==0){
                AddUtils.sendMessage(player,AddUtils.concat("&6[&7工作台&6]&c 电力不足或条件不足! ",String.valueOf(charge),"J/ ",String.valueOf( this.energyConsumption),"J"));
                return;
            }
            limit=Math.min(chargelimit,getCraftLimit(b,inv));
        }else {
            limit=getCraftLimit(b,inv);
        }
        if(limit == 0){
            AddUtils.sendMessage(player,"&6[&7工作台&6]&c 合成失败,请检查合成条件是否均具备");
            return;
        }
        MachineRecipe recipe = CraftUtils.findNextShapedRecipe(inv,getInputSlots(),getOutputSlots(),getMachineRecipes(),
            true,Settings.SEQUNTIAL,CRAFT_PROVIDER);
        if(recipe == null){
            AddUtils.sendMessage(player,"&6[&7工作台&6]&c 合成失败,这不是一个有效的配方");
            return;
        }
        if (this.checkRecipePermission.getValue() && CraftUtils.checkRecipePermission(player, recipe, true)) {
            return;
        }
        //use the directly recipe to perform craft; do not use history indexs
        //indexes are set above
        Pair<MachineRecipe, ItemGreedyConsumer[]> outputResult=
                CraftUtils.matchShapedRecipe(inv,getInputSlots(),getOutputSlots(),List.of(recipe),
                        limit,false,Settings.SEQUNTIAL,CRAFT_PROVIDER);
        if(outputResult != null){
            if(this.energyConsumption > 0){
                int craftTime=outputResult.getSecondValue()[0].getStackNum();
                removeCharge(loc,craftTime*this.energyConsumption);
            }
            CraftUtils.multiUpdateOutputMenu(outputResult.getSecondValue(),inv);
        }
        if(inv.hasViewer()){
            updateMenu(inv,b,Settings.RUN);
        }
    }
    public abstract void updateMenu(BlockMenu blockMenu, Block block, Settings mod);

    public void process(Block b, BlockMenu preset, SlimefunBlockData data){
        if(preset.hasViewer()){
            updateMenu(preset,b,Settings.RUN);
        }
    }
    public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow){
        return flow==ItemTransportFlow.WITHDRAW?getOutputSlots():getInputSlots();
    }
    public int[] getSlotsAccessedByItemTransportPlus(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item){
        if(flow==ItemTransportFlow.WITHDRAW){
            return getOutputSlots();
        }else if(item==null||item.getMaxStackSize()<=1){
            return getInputSlots();
        }
        else{
            List<Integer> input_slots=new ArrayList<Integer>();
            int[] input=getInputSlots();
            for (int i=0;i<input.length;i++){
                if(menu.getItemInSlot(input[i])!=null){
                    input_slots.add(input[i]);
                }
            }
            int[] array = new int[input_slots.size()];

            for (int i = 0; i < input_slots.size(); i++) {
                array[i] = input_slots.get(i);
            }
            return array;

        }
    }
    //modified from INFINITY EXPANSION
    //todo add quick-place last recipe
    protected void moveRecipe(@Nonnull Player player, @Nonnull BlockMenu menu, MachineRecipe machinerecipe, boolean max) {
        ItemStack[] recipe =machinerecipe.getInput();
        PlayerInventory inv = player.getInventory();
        int[] inputs=getRecipeSlots();
        int maxCnt=max?64:1;
        if(recipe.length>inputs.length){
            AddUtils.sendMessage(player,"&c配方大小不匹配,不能在该工作台进行");
            player.closeInventory();
            return;
        }
        ItemConsumer[] itcs=new ItemConsumer[recipe.length];
        ItemStack[] playerInv =inv.getContents();
        ItemPusher[] itps=new ItemPusher[recipe.length];
        ItemPusher[] ips=new ItemPusher[playerInv.length];
        for (int s=0;s<playerInv.length;++s) { //each slot in their inv\
            ips[s]=CraftUtils.getPusher(playerInv[s]);
        }
        boolean[] hasInit=new boolean[recipe.length];
        boolean[] match=new boolean[recipe.length];
        for (int i = 0 ; i < maxCnt; i++) {
            for (int slot = 0 ; slot < recipe.length ; slot++) { //each item in recipe
                boolean firstTime=false;
                if(!hasInit[slot]){
                    itcs[slot]=CraftUtils.getConsumer(recipe[slot]);
                    hasInit[slot]=true;
                    firstTime=true;
                }
                if (itcs[slot] == null) {
                    continue;
                }
                // 重置consumer计数器
                itcs[slot].syncAmount();
                int recipeCount=itcs[slot].getAmount();
                    //获取当前槽位的东西
                if(firstTime){
                    itps[slot]=CraftUtils.getpusher.getPusher(Settings.OUTPUT,menu,inputs[slot]);
                    //不匹配，即非空且不match，就没有下次了
                    if(!itps[slot].isNull()&&!CraftUtils.matchItemCounter(itps[slot],itcs[slot],false)) {
                        continue;
                    }else{//匹配，设置为consumer的
                        match[slot]=true;
                        itps[slot].setFrom(itcs[slot]);
                    }
                }else if(!match[slot]){
                    continue;
                }
                for (int s=0;s<playerInv.length;++s) { //each slot in their inv\
                    if(ips[s]==null||ips[s].getAmount()==0){
                        continue;
                    }//空和为0 直接早退
                    else if ( CraftUtils.matchItemCounter(ips[s],itcs[slot],false)) { //matches recipe
                        //consume,
                        itcs[slot].consume(ips[s]);
                        if(itcs[slot].getAmount()<=0){
                            break;
                        }
                    }
                }
                //try safe push
                //能全放下
                if(itcs[slot].getAmount()<=0&&itps[slot].safeAddAmount(recipeCount)){//push的数量
                    //真的有地方,同步背包里的相关counter
                    itcs[slot].updateItems(menu,Settings.PUSH);
                }else{
                    //放不进去,返回z物品
                    itcs[slot].syncItems();
                }
            }//提前中断,意味着没有后面的了
        }
        for(int i=0;i<recipe.length;++i){
            if(itps[i]!=null){
                itps[i].updateMenu(menu);
            }
        }
        //try set index
        var recipeList = getMachineRecipes();
        int index = recipeList.indexOf(machinerecipe);
        if(index>=0){
            DataCache.setLastRecipe(menu.getLocation(),index);
            updateMenu(menu,menu.getBlock(),Settings.RUN);
        }
        menu.open(player);
    }

}
