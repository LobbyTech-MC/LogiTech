package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import me.matl114.logitech.Utils.UtilClass.ItemClass.ItemGreedyConsumer;

public class MultiCraftingOperation implements CustomMachineOperation {
    private ItemGreedyConsumer[] outputItems;
    private int totalTicks;
    private int currentTicks;
    public MultiCraftingOperation(ItemGreedyConsumer[] outputItems,int time) {
        this.outputItems = outputItems;
        this.totalTicks = time;
        this.currentTicks = 0;
        for (ItemGreedyConsumer outputItem : outputItems) {
            outputItem.clearRelated();
        }
    }


    @Override
	public int getProgress(){
        return this.currentTicks;
    }

    @Override
	public int getRemainingTicks() {
        return this.totalTicks-this.currentTicks;
    }

    public ItemGreedyConsumer[] getResults(){
        return this.outputItems;
    }

    @Override
	public int getTotalTicks(){
        return this.totalTicks;
    }

    @Override
	public boolean isFinished() {
        return this.totalTicks<=this.currentTicks;
    }
    @Override
	public void progress(int var1){
        this.currentTicks += var1;
    }
}
