package me.matl114.logitech.core.Blocks.MultiBlock;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.core.Blocks.MultiBlockCore.MultiBlockPart;
import me.matl114.logitech.utils.AddUtils;
import me.matl114.logitech.utils.CraftUtils;
import me.matl114.logitech.utils.DataCache;
import me.matl114.logitech.utils.TransportUtils;
import me.matl114.logitech.utils.UtilClass.MultiBlockClass.MultiBlockService;
import me.matl114.logitech.core.Machines.Abstracts.AbstractMachine;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MultiIOPort extends AbstractMachine implements MultiBlockPart {
    protected final boolean CONFIG_TO;
    protected final boolean CONFIG_SYMM;
    protected final int[] INPUT_SLOT=new int[]{9,10,11,12,13,14,15,16,17};
    protected final int[] OUTPUT_SLOT=new int[]{9,10,11,12,13,14,15,16,17};
    protected final int[] BORDER=new int[]{0,1,2,3,5,6,7,8};
    protected final ItemStack INFO_ITEM;

    protected final int INFO_SLOT=4;
    public int[] getInputSlots(){
        return INPUT_SLOT;
    }
    public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }

    @Override
    public void process(Block b, BlockMenu preset, SlimefunBlockData data) {

    }

    protected final String BLOCKID;


    public MultiIOPort(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                       ItemStack[] recipe,String blockid,boolean isSym,boolean to) {
        super(itemGroup,item,recipeType,recipe,0,0);
        this.BLOCKID = blockid;
        this.CONFIG_TO = to;
        this.CONFIG_SYMM = isSym;
        this.INFO_ITEM=new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,
                "&a传输器信息", AddUtils.concat( "&7类别: &6",CONFIG_TO?"输入接口":"输出接口"),
              AddUtils.concat(  "&7强对称: &6",CONFIG_SYMM?"是":"否"),
                "&7可配置: &6否");
    }
    public String getPartId(){
        return BLOCKID;
    }
    public void constructMenu(BlockMenuPreset preset){
        preset.setSize(18);
        int[] border = BORDER;
        int len=border.length;
        for(int var4 = 0; var4 < len; ++var4) {

            preset.addItem(border[var4], ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        preset.addItem(INFO_SLOT,INFO_ITEM,ChestMenuUtils.getEmptyClickHandler());
    }
    public void tick(Block b, BlockMenu menu, SlimefunBlockData data, int ticker){
        Location core= MultiBlockService.acceptPartRequest(b.getLocation());
        if(core!=null){
            BlockMenu inv= DataCache.getMenu(core);
            if (inv!=null){
                if(CONFIG_TO){
                    TransportUtils.transportItem(menu,getOutputSlots(),inv,TransportUtils.getInvInputSlot(inv),
                            CONFIG_SYMM,null,576);
                }else {
                    TransportUtils.transportItem(inv,TransportUtils.getInvOutputSlot(inv),menu,getInputSlots(),
                            CONFIG_SYMM,null,576);
                }
            }
        }
    }

    @Override
    public int[] getSlotsAccessedByItemTransportPlus(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        if(CONFIG_SYMM){
            if(flow==ItemTransportFlow.INSERT){
                if(item==null||item.getType().isAir())return getInputSlots();
                int[] slots=getInputSlots();
                for(int i=0;i<getInputSlots().length;++i){
                   ItemStack it=menu.getItemInSlot(slots[i]);
                   if(it!=null&& CraftUtils.matchItemStack(it,item,false)){
                       return new int[]{slots[i]};
                   }
                }
                return slots;
            }else return getOutputSlots();
        }else {
            return super.getSlotsAccessedByItemTransportPlus(menu,flow,item);
        }
    }
    public void preRegister(){
        super.preRegister();
        handleMultiBlockPart(this);
    }
    public void onMultiBlockBreak(BlockBreakEvent e) {
        super.onBreak(e, DataCache.getMenu(e.getBlock().getLocation()));
        MultiBlockPart.super.onMultiBlockBreak(e);
    }

}
