package me.matl114.logitech.SlimefunItem.Blocks.MultiBlock.SmithWorkShop;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import me.matl114.logitech.SlimefunItem.Items.MaterialItem;
import me.matl114.logitech.Utils.AddUtils;
import me.matl114.logitech.Utils.Utils;

public class SWAmplifyComponent extends MaterialItem {
    static HashMap<Material,SWAmplifyComponent> materialJudgement=new HashMap<>();
    public static SWAmplifyComponent getComponentType(ItemStack item){
        return item==null?null: materialJudgement.get(item.getType());
    }
    @Getter
    private Material type;
    public SWAmplifyComponent(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        Preconditions.checkArgument(!materialJudgement.containsKey(item.getType()),"Material of these Component must differ from each other for better judgement performance");
        this.type=item.getType();
        materialJudgement.put(item.getType(), this);
        this.setDisplayRecipes(Utils.list(
                AddUtils.getInfoShow("&f机制",
                        "&7插入至锻铸工坊核心",
                        "&7对四周的工坊接口产生增幅"),
                this.getItem().clone()
        ));

    }
}