package me.matl114.logitech.SlimefunItem.Blocks.MultiBlock;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import me.matl114.logitech.SlimefunItem.Blocks.MultiBlockCore.MultiBlockCore;
import me.matl114.logitech.SlimefunItem.Machines.AbstractAdvancedProcessor;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlock;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlockType;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockHandler;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.CubeMultiBlock.CubeMultiBlock;
import me.matl114.logitech.Utils.UtilClass.RecipeClass.MultiCraftingOperation;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class MultiBlockAdvancedProcessor extends AbstractAdvancedProcessor implements MachineProcessHolder<MultiCraftingOperation>, MultiBlockCore{
    protected final AbstractMultiBlockType MBTYPE;
    protected final String PARTID;
    protected final String HEIGHT_KEY="h";
    protected boolean endOperationWhenBreak=false;
    public MultiBlockAdvancedProcessor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                               ItemStack[] recipe, String blockId, AbstractMultiBlockType type, ItemStack processorItem, int energyConsumption, int energyBuffer,
                               LinkedHashMap<Object, Integer> customRecipes){
        super(itemGroup, item, recipeType, recipe, Material.STONE,energyConsumption,energyBuffer,customRecipes);
        this.processor.setProgressBar(processorItem);
        this.MBTYPE = type;
        this.PARTID = blockId;

    }
    public AbstractMultiBlockHandler createAdvanceProcessor (Location core, AbstractMultiBlock type, String uid){
        AbstractMultiBlockHandler handler=MultiBlockHandler.createHandler(core, type, uid);
        if(handler.getMultiBlock() instanceof CubeMultiBlock cb){
            DataCache.setCustomData(core,HEIGHT_KEY,cb.getHeight());
        }
        return handler;
    }
    @Override
	public MultiBlockService.MultiBlockBuilder getBuilder(){
        return (MultiBlockService.MultiBlockBuilder) this::createAdvanceProcessor;
    }
    @Override
	public int getCraftLimit(Block b,BlockMenu inv){
        return 1<<DataCache.getCustomData(inv.getLocation(),HEIGHT_KEY,0);
    }
    @Override
	public abstract int[] getInputSlots();
    @Override
	public MachineProcessor<MultiCraftingOperation> getMachineProcessor() {
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
