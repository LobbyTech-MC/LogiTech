package me.matl114.logitech.Utils.UtilClass.ItemClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ProbItemStack extends ItemStack implements MultiItemStack,RandOutItem {
    public Random rand=new Random();
    ItemStack stack;
    ItemStack air=new ItemStack(Material.AIR);
    double prob;
    List<ItemStack> stacklist;
    List<Double> problist;
    public ProbItemStack(ItemStack stack,double prob) {
        super(stack);
        this.stack = stack;
        this.stacklist = new ArrayList<>();
        this.problist=new ArrayList<>();
        if(stack instanceof MultiItemStack ms) {
            this.stacklist.addAll(ms.getItemStacks());
            this.problist.addAll(ms.getWeight(prob));
        }else{
            this.stacklist.add(stack);
            this.problist.add(prob);
        }
        this.prob = prob;
    }
    @Override
	public ItemStack clone(){
        if(rand.nextDouble()<this.prob){
            return stack.clone();
        }
        return new ItemStack(Material.AIR);
    }
    @Override
	public ProbItemStack copy(){
        ProbItemStack copystack = new ProbItemStack(stack, prob);
        copystack.stack=this.stack;
        copystack.air=this.air;
        copystack.prob=this.prob;
        copystack.stacklist=new ArrayList<>(this.stacklist);
        copystack.problist=new ArrayList<>(this.problist);
        return copystack;

    }
    @Override
	public ItemStack getInstance(){
        if(rand.nextDouble()<this.prob){
            return  (stack instanceof RandOutItem w)?w.getInstance():stack;
        }
        return this.air;
    }
    @Override
	public List<ItemStack> getItemStacks() {
        return stacklist;
    }
    @Override
	public int getTypeNum(){
        return 1;
    }
    @Override
	public List<Double> getWeight(Double percent){
        List<Double> doubles=new ArrayList<>();
        int len=this.problist.size();
        for (int i=0;i<len;i++){
            doubles.add(this.problist.get(i)*percent);
        }
        return doubles;
    }
    @Override
	public boolean matchItem(ItemStack item,boolean strictCheck){
        return false;
    }
}
