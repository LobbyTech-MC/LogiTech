package me.matl114.logitech.Utils.UtilClass.ItemClass;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * used to cal num when push item, consume-only, it will not push item when unionSum
 */
public class ItemPusher extends ItemCounter {


    private static ItemPusher INSTANCE=new ItemPusher(new ItemStack(Material.STONE));
    public static ItemPusher get(ItemStack item) {
        if(item==null) {
			return null;
		}
        ItemPusher itp=INSTANCE.clone();
        itp.init(item);
        return itp;
    }
    public ItemPusher(){
        super();
    }
    public ItemPusher(ItemStack item) {
        //can use as storage unit
        super(item);

    }
    public ItemPusher(ItemStack item,int maxcnt) {
        //can use as storage unit
        super(item);
        this.maxStackCnt = maxcnt;
    }
    @Override
	protected ItemPusher clone(){
        return (ItemPusher)super.clone();
    }

    @Override
	public void grab(ItemCounter counter) {
        int left=maxStackCnt-cnt;
        if(left>counter.getAmount()){
            addAmount(counter.getAmount());
            counter.setAmount(0);
        }else{
            setAmount(maxStackCnt);
            counter.addAmount(-left);
        }
    }
    @Override
	protected void init( ){
        super.init();
    }
    @Override
	protected void init(ItemStack item){
        super.init( item);
    }

    @Override
	public void push(ItemCounter counter) {
        counter.grab(this);
    }
    public boolean safeAddAmount(int amount){
        int result=amount+cnt;
        if(result>maxStackCnt){
            return false;
        }else {
            setAmount(result);
            return true;
        }
    }


    /**
     * should sync before
     * @param source
     */
    public void setFrom(ItemCounter source){
        if(item==null&&(source!=null&&source.getItem()!=null)){
            item=source.getItem();
            maxStackCnt=item.getMaxStackSize();
            cnt=0;
            meta=null;
        }
    }

    /**
     * transport item From target till limit count,return limit left
     * @param limit
     * @return
     */
    public int transportFrom(ItemCounter counter,int limit){
        //该物品槽能转运的最大数量
        int left=Math.min( maxStackCnt-cnt,limit);
        //如果这个数量比提供的少
        if(left>counter.getAmount()){
            //设置真正被传输的数量是... 提供的数量 小于预期left
            left=counter.getAmount();
            //counter清空
            counter.setAmount(0);
            //加上
            addAmount(left);

        }else{
            //否则这个数量提供的比那个多
            //设置数量+=left
            setAmount(cnt+left);
            //left <= counter.getAmount()
            counter.addAmount(-left);
        }
        return limit-left;
    }
    /**
     * this arguments has no meaning ,just a formated argument
     * @param menu
     */
    public void updateMenu(@Nonnull BlockMenu menu){
        if(dirty){
            updateItemStack();

        }
    }
}
