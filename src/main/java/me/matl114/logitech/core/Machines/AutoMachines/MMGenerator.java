package me.matl114.logitech.core.Machines.AutoMachines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.core.Machines.Abstracts.AbstractTransformer;
import me.matl114.logitech.utils.AddUtils;
import me.matl114.logitech.utils.Algorithms.PairList;
import me.matl114.logitech.utils.MenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MMGenerator extends AbstractTransformer {
    protected final int[] BORDER=new int[]{
            0,1,2,6,7,8
    };
    protected final int[] BORDER_IN=new int[]{
            3,5
    };
    protected final int[] BORDER_OUT=new int[]{
            9,10,11,12,14,15,16,17
    };
    protected final int [] INPUT_SLOT = new int[]{
            4
    };
    protected final int [] OUTPUT_SLOTS=new int[]{
            18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53
    };
    public MMGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                       int time, int energybuffer, int energyConsumption, List<Pair<Object[],Object[]>> outputs_w){
        super(itemGroup,item,recipeType,recipe,time,energybuffer,energyConsumption,
                new PairList<>(){{
                    for(var entry :outputs_w){
                        this.put(new Pair<>(entry.getFirstValue(),entry.getSecondValue()),time-1);
                    }
                }}
                );
        PROCESSOR_SLOT=13;
    }
    public void addInfo(ItemStack stack){
        stack.setItemMeta( AddUtils.smgInfoAdd(stack,time).getItemMeta() );
        super.addInfo(stack);
    }

    public void constructMenu(BlockMenuPreset preset) {
        //空白背景 禁止点击
        int[] border = BORDER;
        int len=border.length;
        for(int var4 = 0; var4 < len; ++var4) {

            preset.addItem(border[var4], ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        //输入槽边框
        border =BORDER_IN;
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
        preset.setSize(54);
    }
    public int[] getInputSlots(){
        return INPUT_SLOT;
    }
    public int[] getOutputSlots(){
        return OUTPUT_SLOTS;
    }
}
