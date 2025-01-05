package me.matl114.logitech.SlimefunItem.Blocks.MultiBlock;

import java.util.LinkedHashMap;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import me.matl114.logitech.SlimefunItem.Blocks.MultiBlockCore.MultiBlockCore;
import me.matl114.logitech.SlimefunItem.Machines.AbstractProcessor;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockType;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.Utils.UtilClass.RecipeClass.SimpleCraftingOperation;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class MultiBlockProcessor extends AbstractProcessor implements MachineProcessHolder<SimpleCraftingOperation>, MultiBlockCore {
    protected final AbstractMultiBlockType MBTYPE;
    protected final String PARTID;
    protected boolean endOperationWhenBreak=false;
    public MultiBlockProcessor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                               ItemStack[] recipe, String blockId, AbstractMultiBlockType type, ItemStack processorItem, int energyConsumption, int energyBuffer,
                               List<Pair<Object, Integer>> customRecipes){
        super(itemGroup, item, recipeType, recipe, processorItem,energyConsumption,energyBuffer,customRecipes);
        this.MBTYPE = type;
        this.PARTID = blockId;

    }
    @Override
	public MultiBlockService.MultiBlockBuilder getBuilder(){
        return MultiBlockHandler::createHandler;
    }

    @Override
	public abstract int[] getInputSlots();
    @Override
	public MachineProcessor<SimpleCraftingOperation> getMachineProcessor() {
        return this.processor;
    }
    @Override
	public AbstractMultiBlockType getMultiBlockType(){
        return MBTYPE;
    }
    @Override
	public abstract int[] getOutputSlots();
    @Override
	public String getPartId(){
        return this.PARTID;
    }
    @Override
	public boolean isSync(){
        return false;
    }
    @Override
	public void onBreak(BlockBreakEvent e, BlockMenu menu){
        super.onBreak(e,menu);
        if(endOperationWhenBreak&& menu!=null){
            this.processor.endOperation(menu.getLocation());
        }
        //合理性:processor可能需要在MultiBlockBreak中处理processor
    }
    @Override
	public void tick(Block b, BlockMenu menu, SlimefunBlockData data, int tickCount){
        //in this case .blockMenu is null
        if(MultiBlockService.acceptCoreRequest(b.getLocation(),getBuilder(),getMultiBlockType())){
            process(b,menu,data);
        }
    }

}
