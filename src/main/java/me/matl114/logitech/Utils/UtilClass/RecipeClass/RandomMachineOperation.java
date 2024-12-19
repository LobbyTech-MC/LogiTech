package me.matl114.logitech.Utils.UtilClass.RecipeClass;

import java.util.Random;

public class RandomMachineOperation implements CustomMachineOperation {
    static Random rand=new Random();
    int totalTicks;
    long key;
    int currentTick;
    public RandomMachineOperation(int time,long key) {
        this.totalTicks = time;
        this.key = key;
        this.currentTick = 0;
    }
    @Override
    public int getProgress() {
        return this.currentTick;
    }
    @Override
	public int getRemainingTicks() {
        return Math.max(0, this.totalTicks-this.currentTick);
    }


    @Override
	public int getTotalTicks(){
        return this.totalTicks;
    }

    @Override
	public void progress(int var1){
    }

    public void randProgress(int amount,long value){
        if(key==value){
            currentTick+=amount;
        }else {
            int offset=((rand.nextInt(0,4)==0)^(key<value))?1:-1;
            currentTick+=amount*offset;
        }
        currentTick=Math.max(currentTick,-1);
    }


}
