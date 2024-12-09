package me.matl114.logitech.SlimefunItem.Blocks.MultiBlock;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import me.matl114.logitech.SlimefunItem.Blocks.MultiBlockCore.MultiBlockCore;
import me.matl114.logitech.SlimefunItem.Machines.AbstractProcessor;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockType;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.Utils.UtilClass.RecipeClass.SimpleCraftingOperation;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class MultiBlockProcessor extends AbstractProcessor implements MachineProcessHolder<SimpleCraftingOperation>, MultiBlockCore {
    protected final AbstractMultiBlockType MBTYPE;
    protected final String PARTID;
    public abstract int[] getInputSlots();
    public abstract int[] getOutputSlots();
    public MultiBlockProcessor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                               ItemStack[] recipe, String blockId, AbstractMultiBlockType type, ItemStack processorItem, int energyConsumption, int energyBuffer,
                               LinkedHashMap<Object, Integer> customRecipes){
        super(itemGroup, item, recipeType, recipe, processorItem,energyConsumption,energyBuffer,customRecipes);
        this.MBTYPE = type;
        this.PARTID = blockId;

    }

    public MachineProcessor<SimpleCraftingOperation> getMachineProcessor() {
        return this.processor;
    }
    public String getPartId(){
        return this.PARTID;
    }
    public AbstractMultiBlockType getMultiBlockType(){
        return MBTYPE;
    }
    public MultiBlockService.MultiBlockBuilder getBuilder(){
        return MultiBlockHandler::createHandler;
    }
    public void onMultiBlockDisable(Location loc, AbstractMultiBlockHandler handler, MultiBlockService.DeleteCause cause){
        processor.endOperation(loc);
        MultiBlockCore.super.onMultiBlockDisable(loc,handler,cause);
    }
    public void tick(Block b, BlockMenu menu, SlimefunBlockData data, int tickCount){
        //in this case .blockMenu is null
        if(MultiBlockService.acceptCoreRequest(b.getLocation(),getBuilder(),getMultiBlockType())){
            process(b,menu,data);
        }
    }
    public boolean isSync(){
        return false;
    }

    public void preRegister(){
        super.preRegister();
        handleMultiBlockPart(this);
    }

}
