package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import me.matl114.logitech.Utils.CraftUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SequenceMachineRecipe extends MachineRecipe {
    public static final String[] displayPrefixs=new String[]{
            "&7⇨ %s%-3d&7/%s","%-3d %s"
    };
    public String[] displayedNames;
    public SequenceMachineRecipe(int ticks, ItemStack[] inputs, ItemStack[] outputs) {
        super(0, inputs, outputs);
        this.setTicks(ticks);
        int len=inputs.length;
        this.displayedNames = new String[len];
        for(int i=0; i<len; i++) {
            displayedNames[i]=new StringBuilder("").append(displayPrefixs[0])
                    .append(displayPrefixs[1].formatted(inputs[i].getAmount(), CraftUtils.getItemName(inputs[i]))).toString();
        }
    }
}
