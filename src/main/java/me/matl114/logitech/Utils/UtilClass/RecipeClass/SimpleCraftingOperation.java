package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import me.matl114.logitech.Utils.UtilClass.ItemClass.ItemConsumer;

public class SimpleCraftingOperation implements CustomMachineOperation {
    protected ItemConsumer[] outputItems;
    protected int totalTicks;
    protected int currentTicks;
    public SimpleCraftingOperation(ItemConsumer[] outputItems,int time) {
        this.outputItems = outputItems;
        this.totalTicks = time;
        this.currentTicks = 0;
    }
    public int getProgress(){
        return this.currentTicks;
    }

    public int getRemainingTicks() {
        return this.totalTicks-this.currentTicks;
    }

    public ItemConsumer[] getResults(){
        return this.outputItems;
    }

    public int getTotalTicks(){
        return this.totalTicks;
    }

    public boolean isFinished() {
        return this.totalTicks<=this.currentTicks;
    }
    public void progress(int var1){
        this.currentTicks += var1;
    }
}