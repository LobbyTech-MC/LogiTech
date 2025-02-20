package me.matl114.logitech.core.Cargo;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.matl114.logitech.core.Machines.Abstracts.AbstractMachine;
import me.matl114.logitech.Utils.*;
import me.matl114.logitech.Utils.Algorithms.PairList;
import me.matl114.logitech.Utils.UtilClass.MenuClass.MenuFactory;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Orientable;
import org.bukkit.inventory.ItemStack;

public class TestStorageUnit extends AbstractMachine {
    private static final int[] INPUT_SLOT = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
    private static final int[] OUTPUT_SLOT = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    public TestStorageUnit(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                            int energybuffer, int energyConsumption){
        super(category,item , recipeType, recipe,energybuffer,energyConsumption);
    }
    public void addInfo(ItemStack item){

    }
    public void constructMenu(BlockMenuPreset preset){
        preset.setSize(54);
        preset.addMenuOpeningHandler((player -> {
            Debug.stackTrace();
        }));
        preset.addMenuClickHandler(18,(player, i, itemStack, clickAction)->{

            new MenuFactory(MenuUtils.SIMPLE_MENU,"测试BYD",4){
                public void init(){
                    setDefaultNPSlots();
                    addInventory(0,new ItemStack(Material.COMMAND_BLOCK));
                }
            }.build().openPages(player,1);
            return false;


        });
        preset.addMenuClickHandler(19,(player,i,itemstack,clickAction)->{
            Debug.logger("override failed");
            return false;
        });
        preset.addMenuClickHandler(19,(player,i,itemstack,clickAction)->{
            Debug.logger("override success");
            return false;
        });
    }
    public void newMenuInstance(BlockMenu inv,Block bLock){
//        DataCache.setLastRecipe(inv.getLocation(),-1);
//        DataCache.setLastLocation(inv.getLocation(),inv.getLocation());\\

        inv.addMenuClickHandler(20,((player, i, itemStack, clickAction) -> {
            Debug.logger("check blockstatemeta", ((Orientable)bLock.getRelative(BlockFace.UP).getBlockData()).getAxis());
            return false;
        }));
        inv.replaceExistingItem(18,new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
        inv.addMenuClickHandler(18,((player, i, itemStack, clickAction) -> {
           Block b=bLock.getRelative(BlockFace.UP);
           Block c= bLock.getRelative(BlockFace.DOWN);
          // b.setBlockData(c.getBlockData());
            if(WorldUtils.copyBlockState(c.getState(false),b)){
                AddUtils.sendMessage(player,"&a好得很");
            }else{
                AddUtils.sendMessage(player,"&a寄了");
            }

//           BlockState state= b.getState();
//           state.setRawData(b.getState().getRawData());
//
//           state.update(true,false);
            return false;
        }));
        inv.addMenuClickHandler(20,((player, i, itemStack, clickAction) -> {
            Location tar=inv.getLocation().clone().add(0.5,1.0,0.5);
//            final DisplayGroup displayGroup = new DisplayGroup(tar);
//            displayGroup.addDisplay(
//                    "StorageUnit_12",
//                    new ItemDisplayBuilder()
//                            .setGroupParentOffset(new Vector(0, 1, 0))
//                            .setItemStack(AddItem.TESTCORE.clone())
//                            .setTransformation(new TransformationBuilder().scale(0.3f,0.3f,0.3f).build())
//                            .build(displayGroup)
//            );
//            Schedules.launchSchedules(Schedules.getRunnable(()->{
//                displayGroup.remove();
//            }),200,true,0);
            return false;
        }));
    }
    public  int[] getInputSlots(){
        return INPUT_SLOT;
    }


    public int[] getOutputSlots(){
        return OUTPUT_SLOT;
    }
    public void process(Block b, BlockMenu menu, SlimefunBlockData data){
        Block c=b.getRelative(BlockFace.UP);
        if (c!=null){
            Debug.logger(c.getType());
            if(!c.getType().isAir()){
                Debug.logger(c.getBlockData());
                Debug.logger(c.getBlockData().getClass());
                Debug.logger(c.getState());
                Debug.logger(c.getState().getClass());
                Debug.logger(c.getState().getData());
                Debug.logger(c.getState().getData().getClass());
            }
        }
    }
    public boolean isSync(){
        return true;
    }

}
