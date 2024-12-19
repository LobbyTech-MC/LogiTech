package me.matl114.logitech.Utils.UtilClass.ItemClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.NotImplementedException;
import me.matl114.logitech.Utils.CraftUtils;

public class EquivalItemStack extends ItemStack implements MultiItemStack ,EqualInItem{
    private static Material getFirstMaterial(HashMap<ItemStack,Integer> map) {
        if(map.isEmpty()) {
            return Material.STONE;
        }else {
            Iterator<Map.Entry<ItemStack,Integer>> iterator = map.entrySet().iterator();
            if(iterator.hasNext()) {
                Map.Entry<ItemStack,Integer> entry = iterator.next();
                return entry.getKey().getType();
            }else {
                return Material.STONE;
            }
        }
    }
    public ItemStack[] itemList;
    public ItemCounter[] counterList;
    public Double[] itemWeight;
    public double[] weightSum;
    public int sum;
    protected int eamount=1;
	private HashMap<ItemStack, Integer> itemSettings;
    public EquivalItemStack(HashMap<ItemStack ,Integer> itemSettings) {
        super(getFirstMaterial(itemSettings));
        this.itemSettings = itemSettings;
        this.sum=itemSettings.keySet().size();
        this.itemList=new ItemStack[sum];
        this.counterList=new ItemCounter[sum];
        this.itemWeight=new Double[sum];
        int weight = itemSettings.size();

        int cnt=0;
        weightSum=new double[sum+1];
        weightSum[0]=0;
        for(Map.Entry<ItemStack, Integer> entry : itemSettings.entrySet()) {
            itemList[cnt] = entry.getKey();
            counterList[cnt]=ItemCounter.get(itemList[cnt]);
            itemWeight[cnt]= 1.0/weight;
            weightSum[cnt+1]=weightSum[cnt]+itemWeight[cnt];
            ++cnt;
        }
    }
    @Override
	public ItemStack clone(){
        return itemList[0].clone();
    }
    @Override
	public EquivalItemStack copy(){
        EquivalItemStack stack = new EquivalItemStack(itemSettings);
        stack.itemList=Arrays.copyOf(this.itemList,this.itemList.length);
        stack.counterList=Arrays.copyOf(this.counterList,this.counterList.length);
        stack.sum=this.sum;
        stack.weightSum=Arrays.copyOf(this.weightSum,this.weightSum.length);
        stack.itemWeight=Arrays.copyOf(this.itemWeight,this.itemWeight.length);
        return stack;

    }
    @Override
	public int getAmount(){
        return this.eamount;
    }
    //递归地解析全体物品列
    @Override
	public List<ItemStack> getItemStacks() {
        List<ItemStack> items = new ArrayList<>();
        for(int i=0;i<sum;i++) {
            if(itemList[i] instanceof MultiItemStack) {
                items.addAll(((MultiItemStack) itemList[i]).getItemStacks());
            }else{
                items.add(itemList[i].clone());
            }
        }
        for (ItemStack item : items) {
            item.setAmount(this.eamount);
        }

        return items;
    }
    @Override
	public int getTypeNum(){
        return this.sum;
    }
    @Override
	public List<Double> getWeight(Double percentage) {
        List<Double> weights = new ArrayList<>();
        for(int i=0;i<sum;i++) {
            if(itemList[i] instanceof MultiItemStack) {
                weights.addAll(((MultiItemStack) itemList[i]).getWeight( itemWeight[i]));
            }else{
                weights.add(itemWeight[i]*percentage);
            }
        }
        return weights;
    }
    @Override
	public boolean matchItem(ItemStack item,boolean strickCheck){
        for (ItemCounter element : counterList) {
            if(CraftUtils.matchItemStack(item,element,strickCheck)){
                return true;
            }
        }
        return false;
    }
    @Override
	public void setAmount(int t){
        throw new NotImplementedException("this method shoudln't be called");
    }
    @Override
	public void setEqualAmount(int t){
        this.eamount=t;
    }
}
