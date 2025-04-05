package me.matl114.logitech.core.Machines.Abstracts;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.utils.*;
import me.matl114.logitech.utils.UtilClass.ItemClass.ItemGreedyConsumer;
import me.matl114.logitech.utils.UtilClass.RecipeClass.MultiCraftingOperation;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AbstractAdvancedProcessor extends AbstractMachine implements MachineProcessHolder<MultiCraftingOperation> {
    protected final int[] BORDER={
            3,4,5,12,13,14,21,22,23,30,31,32,39,40,41,48,49,50
    };
    protected final int[] BORDER_SLOT={
            2,6,11,15,20,24,29,33,38,42,47,51
    };
    protected  final int[] INPUT_SLOT={
            0,1,9,10,18,19,27,28,36,37,45,46
    };
    protected final int[] OUTPUT_SLOT={
            7,8,16,17,25,26,34,35,43,44,52,53
    };
    protected int PROCESSOR_SLOT=22;
    protected final ItemStack progressbar;
    protected final MachineProcessor<MultiCraftingOperation> processor;
    protected final int INFO_SLOT=40;
    public AbstractAdvancedProcessor(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                             Material progressItem, int energyConsumption, int energyBuffer,
                                     List<Pair<Object,Integer>> customRecipes){
        super(category,item , recipeType, recipe,energyBuffer,energyConsumption);

        this.progressbar=new ItemStack(progressItem);
        this.processor = new MachineProcessor(this);
        this.processor.setProgressBar(progressbar);

        if(customRecipes!=null){
            var customRecipes2=AddUtils.buildRecipeMap(customRecipes);
            this.machineRecipes=new ArrayList<>(customRecipes.size());
            for(var recipePiece:customRecipes2){
                //no need to stack and can not stack(maybe some shitmachine will stack
                //but we stack it in order to format up
                this.machineRecipes.add(MachineRecipeUtils.stackFromMachine(
                        new MachineRecipe(recipePiece.getSecondValue(),recipePiece.getFirstValue().getFirstValue(), recipePiece.getFirstValue().getSecondValue())
                ));
            }
        }else{
            this.machineRecipes=new ArrayList<>();
        }

    }

    /**
     * method from MachineProcessorHolder
     * @return
     */
    public MachineProcessor<MultiCraftingOperation> getMachineProcessor() {
        return this.processor;
    }

    public void addInfo(ItemStack stack){

        stack.setItemMeta( AddUtils.machineInfoAdd(
                AddUtils.addLore(stack,"&8⇨ &7并行处理机器"),
                this.energybuffer,this.energyConsumption).getItemMeta());
    }
    /**
     * need implement,  method from MachineProcessorHolder
     * @return
     */
    public ItemStack getProgressBar(){
        return this.progressbar;
    }


    /**
     * cancel machineprocessor after break
     * @param e
     * @param menu
     */
    public void onBreak(BlockBreakEvent e, BlockMenu menu) {
        super.onBreak(e,menu);
        AbstractAdvancedProcessor.this.processor.endOperation(menu.getLocation());
    }

    /**
     *
     * @param preset
     */
    public void constructMenu(BlockMenuPreset preset) {
        //空白背景 禁止点击
        int[] border = BORDER;
        int len=border.length;
        for(int var4 = 0; var4 < len; ++var4) {
            preset.addItem(border[var4], ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        //输入槽边框
        border = BORDER_SLOT;
        len = border.length;
        for(int var4 = 0; var4 <len; ++var4) {

            preset.addItem(border[var4], ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }
        preset.addMenuClickHandler(INFO_SLOT,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(PROCESSOR_SLOT,MenuUtils.PROCESSOR_NULL, ChestMenuUtils.getEmptyClickHandler());
    }
    public int[] getInputSlots(){
        return INPUT_SLOT;
    }

    public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }



    public int getCraftLimit(Block b,BlockMenu inv){
        return 64;
    }
    protected boolean USE_HISTORY=true;
    //fixme 仍要更新界面
    public void process(Block b, BlockMenu inv, SlimefunBlockData data){
        MultiCraftingOperation currentOperation = this.processor.getOperation(b);
        ItemGreedyConsumer[] fastCraft=null;
        if(currentOperation==null){
            int maxCraftlimit=getCraftLimit(b,inv);
            if(maxCraftlimit<=0)return;
            Pair<MachineRecipe,ItemGreedyConsumer[]> nextQ=CraftUtils.matchNextMultiRecipe(inv,getInputSlots(),getMachineRecipes(data),
                    USE_HISTORY,maxCraftlimit, Settings.SEQUNTIAL,CRAFT_PROVIDER);
            if(nextQ==null){
                if(inv.hasViewer()){
                    inv.replaceExistingItem(PROCESSOR_SLOT, MenuUtils.PROCESSOR_NULL);
                }
                return;
            }
            MachineRecipe next=nextQ.getFirstValue();
            int time=next.getTicks();

            if(time!=0){//超频机制
                //尝试让time归1
                //按比例减少maxlimit ,按最小值取craftlimit
                if(maxCraftlimit<=time){
                    time=( (time+1)/maxCraftlimit)-1;
                    maxCraftlimit=1;
                }else {
                    maxCraftlimit=(maxCraftlimit/(time));
                    time=0;
                }
            }
            //最小能减到的刻数
            int craftlimit=CraftUtils.calMaxCraftTime(nextQ.getSecondValue(),maxCraftlimit);
            //要末time=0 要末craftlimit=1 两者在这里都一样,不需要再修改time
            //如果底下这玩意还能给你减,那就craftlimit=0需要考虑,craftlimit=0直接堵上了 否则都一样time=0无区别
            ItemGreedyConsumer[] nextP = CraftUtils.countMultiOutput( nextQ.getSecondValue(),  inv,getOutputSlots(),next,craftlimit,CRAFT_PROVIDER);
            if (nextP != null) {
                CraftUtils.multiUpdateInputMenu(nextQ.getSecondValue(),inv);
                if(time>0){
                    currentOperation = new MultiCraftingOperation(nextP,time);
                    this.processor.startOperation(b, currentOperation);
                }
                else if(time<=0){
                    fastCraft = nextP;
                }

            }else{//if currentOperation ==null return  , cant find nextRecipe
                if(inv.hasViewer()){inv.replaceExistingItem(PROCESSOR_SLOT, MenuUtils.PROCESSOR_NULL);
                }
                return ;
            }

        }
        progressorCost(b,inv);
        if (fastCraft!=null) {
            CraftUtils.multiUpdateOutputMenu(fastCraft,inv);
        }else if(currentOperation.isFinished()){
            ItemGreedyConsumer[] var4=currentOperation.getResults();
            int var5 = var4.length;
            CraftUtils.multiForcePush(var4,inv,getOutputSlots(),CRAFT_PROVIDER);
            if(inv.hasViewer()){
                inv.replaceExistingItem(PROCESSOR_SLOT, MenuUtils.PROCESSOR_NULL);
            }
            this.processor.endOperation(b);
        }
        else{
            if(inv.hasViewer()){
                this.processor.updateProgressBar(inv, PROCESSOR_SLOT, currentOperation);

            }
            currentOperation.progress(1);

        }
    }
    public void preRegister(){
        super.preRegister();
    }
}
