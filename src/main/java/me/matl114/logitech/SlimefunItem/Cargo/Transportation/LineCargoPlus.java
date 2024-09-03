package me.matl114.logitech.SlimefunItem.Cargo.Transportation;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.matl114.logitech.Language;
import me.matl114.logitech.SlimefunItem.Cargo.AbstractCargo;
import me.matl114.logitech.Utils.*;
import me.matl114.logitech.Utils.UtilClass.CargoClass.Directions;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class LineCargoPlus extends LineCargo {
    public LineCargoPlus(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, List<ItemStack> displayList) {
        super(itemGroup, item, recipeType, recipe, displayList);
        setDisplayRecipes(
                Utils.list(AddUtils.getInfoShow("&f机制",
                                "&7这是一个智能货运机器",
                                "&7智能货运机器的行为会包括若干对源方块和目标方块",
                                "&7智能货运机器会进行从源方块到目标方块的物流传输",
                                "&7智能货运机器支持目标方块设置的输入槽限制",
                                "&7但是相应的,其最大传输量会被限制为min(576,配置数)"
                        ),null,
                        AddUtils.getInfoShow("&f机制",
                                "&7本机器可以选择一个方向进行直线搜素",
                                "&&搜索会在方块不存在方块菜单,或至最大搜索距离终止",
                                "&7搜索最大距离为64",
                                "&7在搜索链上的机器会向接下来的机器进行传输",
                                "&7开启首位循环使搜索链上尾部机器向首部机器传输"),null
                )
        );
    }
    public void cargoTask(Block b, BlockMenu menu, SlimefunBlockData data, int configCode){
        Directions dir=getDirection("line_dir",data);
        if(dir==Directions.NONE||dir==null)return;
        //非null 非空
        boolean loop =getConfigValue(0,data)==1;
        Location loc=dir.relate(menu.getLocation());
        BlockMenu inv= StorageCacheUtils.getMenu(loc);
        if(inv!=null){
            HashSet<ItemStack> bwset=new HashSet<>();
            ItemStack it;
            int[] bwslots=getBWListSlot();
            for (int i=0;i<bwslots.length;++i){
                it=menu.getItemInSlot(bwslots[i]);
                if(it!=null){
                    bwset.add(it);
                }
            }
            BlockMenu first=inv;
            BlockMenu next=inv;
            BlockMenu nextTo;
            int limit=MAX_LINE_LEN;
            while(limit>0){
                loc=dir.relate(loc);
                nextTo=StorageCacheUtils.getMenu(loc);
                if(nextTo==null){
                    break;
                }
                TransportUtils.transportItemSmarter(next,nextTo,configCode,bwset);
                next=nextTo;
                --limit;
            }
            if(loop&&limit!=MAX_LINE_LEN){
                TransportUtils.transportItemSmarter(next,first,configCode,bwset);
            }
        }

    }
}