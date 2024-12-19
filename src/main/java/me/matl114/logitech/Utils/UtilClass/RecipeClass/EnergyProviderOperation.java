package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import me.matl114.logitech.Utils.UtilClass.ItemClass.ItemConsumer;

public class EnergyProviderOperation implements CustomMachineOperation {
    private ItemConsumer[] outputItems;
    private int energyPertick;
    private int totalTicks;
    private int currentTicks;
    public EnergyProviderOperation(ItemConsumer[] outputItems,int time,int energyPerTick) {
        this.outputItems = outputItems;
        this.totalTicks = time;
        this.currentTicks = 0;
        this.energyPertick = energyPerTick;
    }
    public int getEnergy(){
        return this.energyPertick;
    }

    @Override
	public int getProgress(){
        return this.currentTicks;
    }

    @Override
	public int getRemainingTicks() {
        return this.totalTicks-this.currentTicks;
    }

    public ItemConsumer[] getResults(){
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
