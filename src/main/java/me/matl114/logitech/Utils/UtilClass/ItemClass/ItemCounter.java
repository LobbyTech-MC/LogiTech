package me.matl114.logitech.Utils.UtilClass.ItemClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCounter implements Cloneable{
    private static ItemCounter INSTANCE=new ItemCounter(new ItemStack(Material.STONE)) ;
    public static ItemCounter get(ItemStack item) {
        ItemCounter consumer=INSTANCE.clone();
        consumer.init(item);
        return consumer;
    }
    protected int cnt;
    protected boolean dirty;
    protected ItemStack item;
    protected ItemMeta meta=null;
    protected int maxStackCnt;
    public ItemCounter() {
        dirty=false;
    }
    protected ItemCounter(ItemStack item) {
        dirty=false;
        this.cnt = item.getAmount();
        this.item=item;
        this.maxStackCnt=item.getMaxStackSize();
        this.maxStackCnt=maxStackCnt<=0?2147483646:maxStackCnt;
    }
    /**
     * modify recorded amount
     * @param amount
     */
    public void addAmount(int amount) {
        cnt += amount;
        dirty=dirty||(amount!=0);
    }
    @Override
	protected ItemCounter clone(){
        ItemCounter clone=null;
        try {
            clone=(ItemCounter) super.clone();
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
    /**
     * consume other counter ,till one of them got zero
     * @param other
     */
    public void consume(ItemCounter other){
        int diff = (other.getAmount()>cnt)?cnt:other.getAmount();
        cnt-=diff;
        dirty=true;
        other.addAmount(-diff);
    }
    /**
     * get recorded amount
     */
    public int getAmount() {
        return cnt;
    }
    /**
     * get item,should be read-only! and will not represent real amount
     * @return
     */
    public ItemStack getItem() {
        return item;
    }

    public int getMaxStackCnt() {
        return maxStackCnt;
    }

    /**
     * get meta info ,if havn't get ,getItemMeta() clone one
     * @return
     */
    public ItemMeta getMeta() {
        if(item.hasItemMeta()){
            if (meta==null){
                meta=  item.getItemMeta();
            }
            return meta;
        }
        return null;
    }
    /**
     * grab other counter till maxSize or sth
     * @param other
     */
    public void grab(ItemCounter other){
        cnt+=other.getAmount();
        dirty=true;
        other.setAmount(0);
    }


    protected void init() {
        this.dirty=false;
        this.cnt=0;
        this.item=null;
        this.maxStackCnt=0;

    }
    protected void init(ItemStack item) {
        this.dirty=false;
        this.item=item;
        this.cnt=item.getAmount();
        this.maxStackCnt=item.getMaxStackSize();
        this.maxStackCnt=maxStackCnt<=0?2147483646:maxStackCnt;
    }
    /**
     * get dirty bits
     * @return
     */
    public boolean isDirty(){
        return dirty;
    }

    public boolean isNull() {
        return item==null;
    }

    /**
     * push to other counter till maxsize or sth
     * @param other
     */
    public void push(ItemCounter other){
        other.grab(this);
    }

    /**
     * modify recorded amount
     * @param amount
     */
    public void setAmount(int amount) {
        dirty=dirty||amount!=cnt;
        cnt=amount;
    }

    public void setDirty(boolean t){
        this.dirty=t;
    }
    /**
     * void constructor
     */

    /**
     * make sure you know what you are doing!
     * @param meta
     */
    public void setMeta(ItemMeta meta) {
        this.meta=meta;
    }

    /**
     * will only sync amount,keep the rest of data unchanged
     */
    public void syncAmount(){
        if(dirty){
            cnt=item.getAmount();
            dirty=false;
        }
    }

    /**
     * will sync amount and other data ,override by subclasses
     */
    public void syncData(){
        if(dirty){
            cnt=item.getAmount();
            dirty=false;
        }
    }

    /**
     * update amount of real itemstack ,or amount of real storage.etc
     */
    public void updateItemStack(){
        if(dirty){

            item.setAmount(cnt);
            dirty=false;
        }
    }

}
