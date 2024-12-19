package me.matl114.logitech.Utils.UtilClass.RecipeClass;

public class TimeCounterOperation implements CustomMachineOperation {
    protected int totalTime;
    protected int currentTime;
    public TimeCounterOperation(int totalTick) {
        this.totalTime = totalTick;
        this.currentTime = 0;
    }
    @Override
    public int getProgress() {
        return currentTime;
    }

    @Override
    public int getTotalTicks() {
        return totalTime;
    }

    public void progress(int k){
        currentTime += k;
    }
}
