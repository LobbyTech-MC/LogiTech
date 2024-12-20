package me.matl114.logitech.Utils;

import java.util.HashSet;
import java.util.function.IntFunction;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.matl114.logitech.Utils.Algorithms.DynamicArray;
import me.matl114.logitech.Utils.UtilClass.CargoClass.CargoConfigs;
import me.matl114.logitech.Utils.UtilClass.ItemClass.ItemPusher;
import me.matl114.logitech.Utils.UtilClass.ItemClass.ItemPusherProvider;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class TransportUtils {
    public static final ItemStack AIR = new ItemStack(Material.AIR);
    public static ItemPusher[] cacheTransportation(BlockMenu cacheInv,ItemPusher[] cachelst,BlockMenu menu,int[] slots,Settings mod){
        return cacheTransportation(cacheInv,cachelst,menu,slots,mod,CraftUtils.getpusher);
    }
    public static ItemPusher[] cacheTransportation(BlockMenu cacheInv,ItemPusher[] cachelst, BlockMenu menu, int[] slots,Settings mod,ItemPusherProvider provider){
        switch (mod){
            case INPUT -> {
                return transportItemToCache(cacheInv, cachelst, menu, slots, provider);
            }
            case OUTPUT -> {
                return transportItemFromCache(cacheInv, cachelst, menu, slots, provider);
            }default -> {
                return null;
            }
        }
    }
    public static void dynamicUpdatePushers(DynamicArray<ItemPusher> pushers,BlockMenu menu){
        int len=pushers.getMaxVisitedIndex();//is index ,not len
        ItemPusher cache;
        for(int i=0;i<=len;++i){
            cache=pushers.get(i);
            if(cache!=null){
                cache.updateMenu(menu);
            }
        }
    }

    public static int[] getInvInputSlot(BlockMenu inv){
        return inv.getPreset().getSlotsAccessedByItemTransport(inv,ItemTransportFlow.INSERT,AIR);
    }
    public static int[] getInvOutputSlot(BlockMenu inv){
        return inv.getPreset().getSlotsAccessedByItemTransport(inv,ItemTransportFlow.WITHDRAW,AIR);
    }
    public static boolean inbwlist(HashSet<ItemStack> bwset,ItemPusher pusher){
        if(bwset==null||bwset.isEmpty()){return false;}
        for(ItemStack item:bwset){

            if(CraftUtils.matchItemStack(item,pusher,false)){

                return true;
            }
        }return false;
    }
/**
    public static void transportItemGreedy(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,
                                           boolean isnull,boolean lazy,boolean whitlist, HashSet<ItemStack> blacklist, int translimit,
                                           ItemPusherProvider provider){
        List<ItemPusher> record=new ArrayList<>(6);
        int recordlen=0;
        int len=fromSlot.length;
        //T 从1开始，from槽位中的物品会被标记， 标记为相同的说明match
        //TOD 如果与某个输出里的槽位match 如果它有一个先前的编号 则将当前item设置为这个编号
        //TOD 若没有标记或者空槽将会被push，会标记最后一个为id，因为一次匹配链最多只有一个非满槽位出现
        //TOD 设置为-1会让后续跳过匹配 比如黑名单或者槽位已满
        //TOD 当前问题 重复获取了太多满槽,导致开销过大,
        int len2=toSlot.length;
        int[] toRecord=new int[len2];
        //输入输出槽的缓存，将会在这里进行数量操作,最终同步
        DynamicArray<ItemPusher> fromCache=new DynamicArray<>(ItemPusher[]::new,len,(i)->(provider.get(Settings.INPUT,from,fromSlot[i])));
        //ItemPusher[] fromCache=new ItemPusher[len];
        DynamicArray<ItemPusher> toCache=new DynamicArray<>(ItemPusher[]::new,len2,(i)->(provider.get(Settings.OUTPUT,to,toSlot[i])));
        //ItemPusher[] toCache=new ItemPusher[len2];
        //记录转运的数量
        //index-slot对应表,用于在toSlot里面因物品和flow而异的输入槽槽位数反向找到index
        HashMap<Integer,Integer> slot2index=new HashMap<>();
        for(int i=0;i<len2;++i){
            //toCache[i]=provider.get(Settings.OUTPUT,to,toSlot[i]);
            slot2index.put(toSlot[i],i);
        }
        ItemPusher fromPusher ;
        ItemPusher toPusher;
        loop:
        for(int i=0;i<len;++i){
            fromPusher=fromCache.get(i);
            //只有 黑名单模式且黑名单不包含 或者 白名单模式或者白名单包含 时候 才通过
            //不通过： 黑名单模式且黑名单包含 白名单模式且白名单不包含
            if(fromPusher==null||(inbwlist(blacklist,fromPusher)^whitlist)){//判断输入是不是空或者黑名单
                continue;
            }
            //查询是否和之前推送的物品一样
            int getRecordIndex=0;
            boolean hasGotRecord=false;
            //获得可推送的槽位，其实基本上所有机器都是一样的输出槽,小部分有限制会不一样
            //TOD 将此处遍历变成可变长度 减少后续搜索开销。并且支持快速删除
            //TOD 需要实现简易链表
            for(int index=0;index<toSlot.length;++index){
                //
                if(toRecord[index]!=-1){
                    toPusher=toCache.get(index);
                    //匹配，空的或者使用历史记录
                    if(toPusher.getItem()==null){
                        //目标槽位为空,设置为当前物品
                        toPusher.setFrom(fromPusher);
                        //转运方法,
                        translimit=toPusher.transportFrom(fromPusher,translimit);
                        //设置历史,如果为空按理说应该没有先前的设置过
                        toRecord[index]=getRecordIndex;
                    }else if(isnull||toPusher.getAmount()==toPusher.getMaxStackCnt()){
                        //目标槽位已满,直接跳过
                        //非空模式 直接跳过
                        toRecord[index]=-1;
                        continue;
                    }else {
                        if(!hasGotRecord){//不存在类型记录,至少暂时的
                            if(CraftUtils.matchItemCounter(fromPusher,toPusher,false)){//如果匹配
                                translimit=toPusher.transportFrom(fromPusher,translimit);
                                //尝试维护历史记录,如果恰好匹配上了,那会挺不错的
                                if(toRecord[index]!=0){
                                    hasGotRecord=true;
                                    getRecordIndex=toRecord[index];
                                }
                            }
                        }else{//存在类型记录
                            //需要历史记录的部分
                            if(toRecord[index]!=0&&toRecord[index]!=getRecordIndex){
                                continue;
                            }//匹配，空的或者使用历史记录
                            else if(toRecord[index]!=0){//使用历史记录
                                translimit=toPusher.transportFrom(fromPusher,translimit);
                            //直接转运,不修改历史记录
                            }else if(CraftUtils.matchItemCounter(fromPusher,toPusher,false)){//没有历史记录.新的,要match
                                translimit=toPusher.transportFrom(fromPusher,translimit);
                                //物品匹配,进行转运，记录历史记录
                                toRecord[index]=getRecordIndex;
                            }
                        }
                    }
                    //结束了,,
                    //没有转运份额了,终结
                    if(translimit==0){
                        break loop;
                    }
                    //当前堆用完了,终结
                    //这里一定是上次没有这次有的 说明该槽位匹配，记录历史
                    if(fromPusher.getAmount()<=0){
                        if(!hasGotRecord){
                            //合理性,我们是按顺序推送的,既然是按顺序,那么在一次推送序列中他就最多最终会有一个不满64的槽位与之匹配并且一定是最后一个
                            //因为如果存在前面的 那他应该在前面被填满 所以这一定是最后一个
                            //如果没有record 只需要对最后一个设置新的reord就能记录上全部的信息
                            //当然如果找到了record想必在比较的时候就已经记录了
                            record.add(fromPusher);
                            recordlen+=1;
                            toRecord[index]=recordlen;
                        }
                        break;
                    }
                }
            }
            if(lazy&&fromPusher.isDirty()){
                //当前非空 且被运输了 并且是首位阻断模式 直接退出
                break ;
            }
            //不一样则新增record,下次用
        }
        //先更新输出槽的，避免出现输出槽设置的source被人销户了
        dynamicUpdatePushers(toCache,to);
        dynamicUpdatePushers(fromCache,from);
    }
**/
/**
 *
 */
    public static void transportItem(BlockMenu from,BlockMenu to ,int configCode,boolean smart,HashSet<ItemStack> bwlist,ItemPusherProvider provider){
        boolean from_input= CargoConfigs.FROM_INPUT.getConfig(configCode);
        boolean to_output= CargoConfigs.TO_OUTPUT.getConfig(configCode);

        boolean reverse=CargoConfigs.REVERSE.getConfig(configCode);
        if(!reverse){
            int[] fromSlot=from_input?
                    from.getPreset().getSlotsAccessedByItemTransport(from,ItemTransportFlow.INSERT,AIR)
                    :from.getPreset().getSlotsAccessedByItemTransport(from,ItemTransportFlow.WITHDRAW,AIR);
            ItemTransportFlow flow=to_output?ItemTransportFlow.WITHDRAW:ItemTransportFlow.INSERT;
            int[] toSlot= to.getPreset().getSlotsAccessedByItemTransport(to,flow,AIR);
            //只有目标INSERT才需要
            smart=smart&&flow==ItemTransportFlow.INSERT;
            transportItem(from,fromSlot,to,toSlot,configCode,smart,bwlist,provider);
        }else {
            int[] toSlot=to_output?
                    to.getPreset().getSlotsAccessedByItemTransport(to,ItemTransportFlow.WITHDRAW,AIR)
                    :to.getPreset().getSlotsAccessedByItemTransport(to,ItemTransportFlow.INSERT,AIR);
            ItemTransportFlow flow=from_input?ItemTransportFlow.INSERT:ItemTransportFlow.WITHDRAW;
            int[] fromSlot= from.getPreset().getSlotsAccessedByItemTransport(from,flow,AIR);
            //只有目标INSERT才需要
            smart=smart&&flow==ItemTransportFlow.INSERT;
            transportItem(to,toSlot,from,fromSlot,configCode,smart,bwlist,provider);
        }
    }
    /**
     * standard method
     * @param from
     * @param fromSlot
     * @param to
     * @param toSlot
     * @param isSymm
     * @param blacklist
     * @param translimit
     */
    public static void transportItem(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,boolean isSymm,
                                     HashSet<ItemStack> blacklist, int translimit){
        if(isSymm){
            transportItemSymm(from,fromSlot,to,toSlot,false,false,false,blacklist,translimit,CraftUtils.getpusher);
        }else {
            transportItemGreedy_2(from,fromSlot,to,toSlot,false,false,false,blacklist,translimit,CraftUtils.getpusher);
        }
    }
    public static void transportItem(BlockMenu from,int[] fromSlot,BlockMenu to,int[] toSlot,int configCode,
            boolean smart, HashSet<ItemStack> bwList,ItemPusherProvider provider){
        boolean issymm=CargoConfigs.IS_SYMM.getConfig(configCode);
        boolean isnull=CargoConfigs.IS_NULL.getConfig(configCode);
        boolean islazy=CargoConfigs.IS_LAZY.getConfig(configCode);
        boolean iswtlist=CargoConfigs.IS_WHITELST.getConfig(configCode);
        int limit=CargoConfigs.TRANSLIMIT.getConfigInt(configCode);
        if(issymm){
            transportItemSymm(from,fromSlot,to,toSlot,isnull,islazy,iswtlist,bwList,limit,provider);
        }else{
            if(!smart){
                transportItemGreedy_2(from,fromSlot,to,toSlot,isnull,islazy,iswtlist,bwList,limit,provider);
            }else {
                limit=Math.min(limit,576);
                transportItemSmart_2(from,fromSlot,to,toSlot,isnull,islazy,iswtlist,bwList,limit,provider);
            }
        }
    }
    /**
     * transportation from cache to slots ,will not update cache Amount to menu,will update slots to menu
     * @param menu
     * @param slots
     * @param provider
     * @return
     */
    public static ItemPusher[] transportItemFromCache(BlockMenu cacheInv,ItemPusher[] cachelst, BlockMenu menu, int[] slots, ItemPusherProvider provider){
        int len=slots.length;
        ItemPusher tarCounter2;
        ItemPusher[] tarCounter=new ItemPusher[len];
        int len2=cachelst.length;
        IntFunction<ItemPusher> fromPusherFunc=provider.getMenuInstance(Settings.OUTPUT,menu,slots);
        for(int j=0;j<len2;++j){
            ItemPusher cache=cachelst[j];
            if(cache==null){
                continue;
            }
            for(int i=0;i<len;i++){
                if(tarCounter[i]==null){
                    tarCounter[i]=fromPusherFunc.apply(i);  //provider.get(Settings.OUTPUT,menu,slots[i]);
                }
                tarCounter2=tarCounter[i];
                //quick pass
                if(tarCounter2.getItem()==null){
                    tarCounter2.setFrom(cache);
                    tarCounter2.grab(cache);
                }
                else if(tarCounter2.getAmount()>=tarCounter2.getMaxStackCnt()) {
					continue;
				} else if(CraftUtils.matchItemCounter(tarCounter2,cache,true)){
                    tarCounter2.grab(cache);
                }
                if(cache.getAmount()==0){
                    break;
                }
            }
        }
        updatePushers(tarCounter,menu);
        return tarCounter;
        //代表是否全部推送完毕
    }

    public static void transportItemGeneral(BlockMenu from, BlockMenu to, int ConfigCode, HashSet<ItemStack> bwlist){
        transportItem(from,to,ConfigCode,false,bwlist,CraftUtils.getpusher);
    }
    /**
     * second version of transportGreedy, greate improvement of fullsize destination transportation
     * @param from
     * @param fromSlot
     * @param to
     * @param toSlot
     * @param isnull
     * @param lazy
     * @param whitlist
     * @param blacklist
     * @param translimit
     * @param provider
     */
    //FIXME transport illegalStack failed
    //FIXME should transport illegalStack from inputs to null slot
    public static void transportItemGreedy_2(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,
                                             boolean isnull,boolean lazy,boolean whitlist, HashSet<ItemStack> blacklist, int translimit,
                                             ItemPusherProvider provider){
        int len=fromSlot.length;
        int len2=toSlot.length;
        int[] toCacheIndex=new int[len2];
        int indexLen=0;
        ItemStack stack;
        for(int i=0;i<len2;++i){
            stack=to.getItemInSlot(toSlot[i]);
            //如果是空 则大概率要考虑这玩意
            if(stack==null){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
            }
            //如果不是非空模式 且非满 才要考虑这玩意
            else if(!isnull&&stack.getAmount()<stack.getMaxStackSize()){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
            }
        }
        //我那么大一个输出槽呢？
        if(indexLen<=0) {
			return;
		}
        DynamicArray<ItemPusher> toCache=new DynamicArray<>(ItemPusher[]::new,indexLen,provider.getMenuInstance(Settings.OUTPUT,to,toCacheIndex));
        boolean[] toRecord=new boolean[indexLen];
        ItemPusher fromPusher ;
        ItemPusher toPusher;
        int fromAmount;
        int maxSizeSlotTill=0;
        IntFunction<ItemPusher> fromPusherFunc=provider.getMenuInstance(Settings.INPUT,from,fromSlot);
        loop:
        for(int i=0;i<len;++i){
            fromPusher=fromPusherFunc.apply(i);
            if(fromPusher==null||(inbwlist(blacklist,fromPusher)^whitlist)){
                continue;
            }
            for(int index=maxSizeSlotTill;index<indexLen;++index){
                if(!toRecord[index]){
                    toPusher=toCache.get(index);
                    if(toPusher.getItem()==null){
                        toPusher.setFrom(fromPusher);
                        //转运方法,
                        fromAmount=Math.min(translimit, fromPusher.getAmount());
                        toPusher.setAmount(fromAmount);
                        translimit-=fromAmount;
                        //拆开写转运
                        //
                        if( fromAmount>=fromPusher.getMaxStackCnt()){
                            //满了 或者非空了 ?,全转,下次别来了
                            fromPusher.setAmount(0);
                            //标记为true说明已经转完了
                            toRecord[index]=true;
                            maxSizeSlotTill+=(index==maxSizeSlotTill)?1:0;
                        }else {
                            fromPusher.addAmount(-fromAmount);
                            //设置历史,如果为空按理说应该没有先前的设置过
                        }
                        toPusher.updateMenu(to);
                        fromPusher.updateMenu(from);
                        //FME 非空逻辑有问题 需要在获取槽位的时候剔除掉
                    }else {
                        if(CraftUtils.matchItemCounter(fromPusher,toPusher,true)){//如果匹配
                            translimit=toPusher.transportFrom(fromPusher,translimit);
                            //DO 将update改到不同的地方 不要做存储
                            toPusher.updateMenu(to);
                            fromPusher.updateMenu(from);
                        }
                    }
                    //结束了,,
                    //没有转运份额了,终结
                    if(translimit==0){
                        break loop;
                    }
                    if(fromPusher.getAmount()<=0){
                        break;
                    }
                }
            }
            if(lazy&&fromPusher.isDirty()){
                //当前非空 且被运输了 并且是首位阻断模式 直接退出
                break ;
            }
            //不一样则新增record,下次用
        }
    }


/**
    public static void transportItemGreedy_2_workspace(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,
                                           boolean isnull,boolean lazy,boolean whitlist, HashSet<ItemStack> blacklist, int translimit,
                                           ItemPusherProvider provider){
        //int recordlen=0;
        int len=fromSlot.length;
        //TOD 从1开始，from槽位中的物品会被标记， 标记为相同的说明match
        //TOD 如果与某个输出里的槽位match 如果它有一个先前的编号 则将当前item设置为这个编号
        //TOD 若没有标记或者空槽将会被push，会标记最后一个为id，因为一次匹配链最多只有一个非满槽位出现
        //TOD 设置为-1会让后续跳过匹配 比如黑名单或者槽位已满
        //TOD 当前问题 重复获取了太多满槽,导致开销过大,
        int len2=toSlot.length;
        int[] toRecord=new int[len2];
        //输入输出槽的缓存，将会在这里进行数量操作,最终同步
        //DynamicArray<ItemPusher> fromCache=new DynamicArray<>(ItemPusher[]::new,len,(i)->(provider.get(Settings.INPUT,from,fromSlot[i])));
        //ItemPusher[] fromCache=new ItemPusher();
        //ItemPusher[] fromCache=new ItemPusher[len];
        int[] toCacheIndex=new int[len2];
        int indexLen=0;
        ItemStack stack;
        for(int i=0;i<len2;++i){
            stack=to.getItemInSlot(toSlot[i]);
            if(stack.getAmount()!=stack.getMaxStackSize()){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
            }
        }

        DynamicArray<ItemPusher> toCache=new DynamicArray<>(ItemPusher[]::new,indexLen,(i)->(provider.get(Settings.OUTPUT,to,toCacheIndex[i])));
        //ItemPusher[] toCache=new ItemPusher[len2];
        //记录转运的数量
        //index-slot对应表,用于在toSlot里面因物品和flow而异的输入槽槽位数反向找到index
        ItemPusher fromPusher ;
        ItemPusher toPusher;
        int fromAmount;
        int toAmount;
        loop:
        for(int i=0;i<len;++i){
            fromPusher=provider.get(Settings.INPUT,from,fromSlot[i]);
            //只有 黑名单模式且黑名单不包含 或者 白名单模式或者白名单包含 时候 才通过
            //不通过： 黑名单模式且黑名单包含 白名单模式且白名单不包含
            if(fromPusher==null||(inbwlist(blacklist,fromPusher)^whitlist)){//判断输入是不是空或者黑名单
                continue;
            }
            //查询是否和之前推送的物品一样
            //int getRecordIndex=0;
            //boolean hasGotRecord=false;
            //获得可推送的槽位，其实基本上所有机器都是一样的输出槽,小部分有限制会不一样
            //TOD 将此处遍历变成可变长度 减少后续搜索开销。并且支持快速删除
            //TOD 需要实现简易链表
            for(int index=0;index<indexLen;++index){
                //
                if(toRecord[index]!=-1){
                    toPusher=toCache.get(index);
                    //匹配，空的或者使用历史记录
                    if(toPusher.getItem()==null){
                        //目标槽位为空,设置为当前物品
                        toPusher.setFrom(fromPusher);
                        //转运方法,
                        fromAmount=Math.min(translimit, fromPusher.getAmount());
                        toPusher.setAmount(fromAmount);
                        translimit-=fromAmount;
                        //translimit=toPusher.transportFrom(fromPusher,translimit);
                        //拆开写转运
                        //TOD 将update改到不同的地方 不要做存储
                        if(fromAmount==fromPusher.getMaxStackCnt()){
                            //满了,全转,下次别来了
                            fromPusher.setAmount(0);
                            toRecord[index]=-1;
                        }else {
                            fromPusher.addAmount(-fromAmount);
                            //设置历史,如果为空按理说应该没有先前的设置过
                           // toRecord[index]=getRecordIndex;
                        }
                        toPusher.updateMenu(to);
                        fromPusher.updateMenu(from);
                    }else if(isnull){
                        //非空模式 直接跳过
                        toRecord[index]=-1;
                        continue;
                    }else {//存在东西 需要考虑该怎么推送的问题
                        //TOD 为什么这个record是没有必要的
                        //TOD 证据：首先 在场上只会有最多一个非满的某个record位
                        //TOD 因为一开始都没有
                        //TOD 你需要通过比较才能出 而每次最多出一个
                        //TOD 但是你作为fromCache第一次比较就必定要比到这个record 所以有和没有都一样。
                        //TOD 所以删除
                        //if(!hasGotRecord){//不存在类型记录,至少暂时的
                        if(CraftUtils.matchItemCounter(fromPusher,toPusher,false)){//如果匹配
                            translimit=toPusher.transportFrom(fromPusher,translimit);
                            //TOD 将update改到不同的地方 不要做存储
                            toPusher.updateMenu(to);
                            fromPusher.updateMenu(from);
                            //尝试维护历史记录,如果恰好匹配上了,那会挺不错的
//                            if(toRecord[index]!=0){
////                            hasGotRecord=true;
////                            getRecordIndex=toRecord[index];
//                            }
                        }
//                        }else{//存在类型记录
//                            //需要历史记录的部分
//                            if(toRecord[index]!=0&&toRecord[index]!=getRecordIndex){
//                                continue;
//                            }//匹配，空的或者使用历史记录
//                            else if(toRecord[index]!=0){//使用历史记录
//                                translimit=toPusher.transportFrom(fromPusher,translimit);
//
//                                toPusher.updateMenu(to);
//                                fromPusher.updateMenu(from);
//                                //直接转运,不修改历史记录
//                            }else if(CraftUtils.matchItemCounter(fromPusher,toPusher,false)){//没有历史记录.新的,要match
//                                translimit=toPusher.transportFrom(fromPusher,translimit);
//                                // 将update改到不同的地方 不要做存储
//                                toPusher.updateMenu(to);
//                                fromPusher.updateMenu(from);
//                                //物品匹配,进行转运，记录历史记录
//                                toRecord[index]=getRecordIndex;
//                            }
//                        }
                    }
                    //结束了,,
                    //没有转运份额了,终结
                    if(translimit==0){
                        break loop;
                    }
                    //当前堆用完了,终结
                    //这里一定是上次没有这次有的 说明该槽位匹配，记录历史
                    if(fromPusher.getAmount()<=0){
//                        if(!hasGotRecord){
//                            //合理性,我们是按顺序推送的,既然是按顺序,那么在一次推送序列中他就最多最终会有一个不满64的槽位与之匹配并且一定是最后一个
//                            //因为如果存在前面的 那他应该在前面被填满 所以这一定是最后一个
//                            //如果没有record 只需要对最后一个设置新的reord就能记录上全部的信息
//                            //当然如果找到了record想必在比较的时候就已经记录了
//                            record.add(fromPusher);
//                            recordlen+=1;
//                            toRecord[index]=recordlen;
//                        }
                        break;
                    }
                }
            }
            if(lazy&&fromPusher.isDirty()){
                //当前非空 且被运输了 并且是首位阻断模式 直接退出
                break ;
            }
            //不一样则新增record,下次用
        }
        //先更新输出槽的，避免出现输出槽设置的source被人销户了
        //dynamicUpdatePushers(toCache,to);
        //dynamicUpdatePushers(fromCache,from);
    }
**/


    //#TODO more improve plan
    public static void transportItemGreedy_3_workspace(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,
                                             boolean isnull,boolean lazy,boolean whitlist, HashSet<ItemStack> blacklist, int translimit,
                                             ItemPusherProvider provider){
        int len=fromSlot.length;
        int len2=toSlot.length;
        int[] toCacheIndex=new int[len2];
        int indexLen=0;
        ItemStack stack;
        //TOO what if we do loop on outputslot first
        //TDO 调整for顺序 将输出作为主循环 输入作为
        //TDO 这样output槽可以不生成cache？ 其实我觉得生成的也没啥
        //TDO 增加一个动态迭代器 或者动态获取的什么玩意
        //TDO 还是先整个最简单的
        for(int i=0;i<len2;++i){
            stack=to.getItemInSlot(toSlot[i]);
            //如果是空 则大概率要考虑这玩意
            if(stack==null){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
            }
            //如果不是非空模式 且非满 才要考虑这玩意
            else if(!isnull&&stack.getAmount()<stack.getMaxStackSize()){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
            }
        }
        //我那么大一个输出槽呢？
        if(indexLen<=0) {
			return;
		}
        DynamicArray<ItemPusher> toCache=new DynamicArray<>(ItemPusher[]::new,indexLen,provider.getMenuInstance(Settings.OUTPUT,to,toCacheIndex));
        boolean[] toRecord=new boolean[indexLen];
        ItemPusher fromPusher ;
        ItemPusher toPusher;
        int fromAmount;
        IntFunction<ItemPusher> fromPusherFunc=provider.getMenuInstance(Settings.INPUT,from,fromSlot);
        loop:
        for(int i=0;i<len;++i){
            fromPusher=fromPusherFunc.apply(i);
            if(fromPusher==null||(inbwlist(blacklist,fromPusher)^whitlist)){
                continue;
            }
            for(int index=0;index<indexLen;++index){
                if(!toRecord[index]){
                    toPusher=toCache.get(index);
                    if(toPusher.getItem()==null){
                        toPusher.setFrom(fromPusher);
                        //转运方法,
                        fromAmount=Math.min(translimit, fromPusher.getAmount());
                        toPusher.setAmount(fromAmount);
                        translimit-=fromAmount;
                        //拆开写转运
                        //
                        if( fromAmount>=fromPusher.getMaxStackCnt()){
                            //满了 或者非空了 ?,全转,下次别来了
                            fromPusher.setAmount(0);
                            //标记为true说明已经转完了
                            toRecord[index]=true;
                        }else {
                            fromPusher.addAmount(-fromAmount);
                            //设置历史,如果为空按理说应该没有先前的设置过
                        }
                        toPusher.updateMenu(to);
                        fromPusher.updateMenu(from);
                        // 非空逻辑有问题 需要在获取槽位的时候剔除掉
                    }else {
                        if(CraftUtils.matchItemCounter(fromPusher,toPusher,true)){//如果匹配
                            translimit=toPusher.transportFrom(fromPusher,translimit);
                            //T 将update改到不同的地方 不要做存储
                            toPusher.updateMenu(to);
                            fromPusher.updateMenu(from);
                        }
                    }
                    //结束了,,
                    //没有转运份额了,终结
                    if(translimit==0){
                        break loop;
                    }
                    if(fromPusher.getAmount()<=0){
                        break;
                    }
                }
            }
            if(lazy&&fromPusher.isDirty()){
                //当前非空 且被运输了 并且是首位阻断模式 直接退出
                break ;
            }
            //不一样则新增record,下次用
        }
    }

    /**
     * use slot limit of getSlotAccess
     * @param from
     * @param fromSlot
     * @param to
     * @param toSlot
     * @param isnull
     * @param lazy
     * @param whitlist
     * @param blacklist
     * @param translimit
     * @param provider
     */
    //FIXME transport illegalStack failed
    //FIXME should transport illegalStack from inputs to null slot
    public static void transportItemSmart_2(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,
                                             boolean isnull,boolean lazy,boolean whitlist, HashSet<ItemStack> blacklist, int translimit,
                                             ItemPusherProvider provider){
        int len=fromSlot.length;
        int len2=toSlot.length;
        int[] toCacheIndex=new int[len2];
        int indexLen=0;
        ItemStack stack;
        int[] slotMappers=new int[54];//we assert that slot is under 54
        for(int i=0;i<len2;++i){
            stack=to.getItemInSlot(toSlot[i]);
            //如果是空 则大概率要考虑这玩意
            if(stack==null){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
                slotMappers[toSlot[i]]=indexLen;//以区别0
            }
            //如果不是非空模式 且非满 才要考虑这玩意
            else if(!isnull&&stack.getAmount()<stack.getMaxStackSize()){
                toCacheIndex[indexLen]=toSlot[i];
                ++indexLen;
                slotMappers[toSlot[i]]=indexLen;//+1.，以区别0
            }
        }
        //我那么大一个输出槽呢？
        if(indexLen<=0) {
			return;
		}
        DynamicArray<ItemPusher> toCache=new DynamicArray<>(ItemPusher[]::new,indexLen,provider.getMenuInstance(Settings.OUTPUT,to,toCacheIndex));
        boolean[] toRecord=new boolean[indexLen];
        ItemPusher fromPusher ;
        ItemPusher toPusher;
        int fromAmount;
        int[] restrictedInsertSlot;
        int index;
        IntFunction<ItemPusher> fromPusherFunc=provider.getMenuInstance(Settings.INPUT,from,fromSlot);

        loop:
        for(int i=0;i<len;++i){
            fromPusher=fromPusherFunc.apply(i);       //  provider.get(Settings.INPUT,from,fromSlot[i]);
            if(fromPusher==null||(inbwlist(blacklist,fromPusher)^whitlist)){
                continue;
            }
            restrictedInsertSlot=to.getPreset().getSlotsAccessedByItemTransport(to,ItemTransportFlow.INSERT,fromPusher.getItem());
            int restrictedLen=restrictedInsertSlot.length;
            for(int j=0;j<restrictedLen;++j){
                index=slotMappers[restrictedInsertSlot[j]]-1;
                //not in available slots
                if(index<0) {
					continue ;
				}
                if(!toRecord[index]){
                    toPusher=toCache.get(index);
                    if(toPusher.getItem()==null){
                        toPusher.setFrom(fromPusher);
                        //转运方法,
                        fromAmount=Math.min(translimit, fromPusher.getAmount());
                        toPusher.setAmount(fromAmount);
                        translimit-=fromAmount;
                        //拆开写转运
                        //
                        if( fromAmount>=fromPusher.getMaxStackCnt()){
                            //满了 或者非空了 ?,全转,下次别来了
                            fromPusher.setAmount(0);
                            //标记为true说明已经转完了
                            toRecord[index]=true;
                        }else {
                            fromPusher.addAmount(-fromAmount);
                            //设置历史,如果为空按理说应该没有先前的设置过
                        }
                        toPusher.updateMenu(to);
                        fromPusher.updateMenu(from);
                        //E 非空逻辑有问题 需要在获取槽位的时候剔除掉
                    }else {
                        if(CraftUtils.matchItemCounter(fromPusher,toPusher,true)){//如果匹配
                            translimit=toPusher.transportFrom(fromPusher,translimit);
                            //TO 将update改到不同的地方 不要做存储
                            toPusher.updateMenu(to);
                            fromPusher.updateMenu(from);
                        }
                    }
                    //结束了,,
                    //没有转运份额了,终结
                    if(translimit==0){
                        break loop;
                    }
                    if(fromPusher.getAmount()<=0){
                        break;
                    }
                }
            }
            if(lazy&&fromPusher.isDirty()){
                //当前非空 且被运输了 并且是首位阻断模式 直接退出
                break ;
            }
            //不一样则新增record,下次用
        }
    }


    public static void transportItemSmarter(BlockMenu from, BlockMenu to, int ConfigCode, HashSet<ItemStack> bwlist){
       transportItem(from,to,ConfigCode,true,bwlist,CraftUtils.getpusher);
    }





    //FIXME translimit cal wrong
    public static void transportItemSymm(BlockMenu from, int[] fromSlot, BlockMenu to, int[] toSlot,
                                         boolean isnull,boolean lazy,boolean whitlist, HashSet<ItemStack> blacklist, int translimit,
                                         ItemPusherProvider provider){
        int len=fromSlot.length;
        //从1开始，标记为相同的说明match
        //空槽将被标记为相同的并且会被push
        //如果不匹配,那就全是0作为初始值
        //如果是黑名单物品则会被设置为-1,null会被设置为-2
        int len2=toSlot.length;
        int transSlot=Math.min(len2,len);
        IntFunction<ItemPusher> fromPusherFunc=provider.getMenuInstance(Settings.INPUT,from,fromSlot);
        IntFunction<ItemPusher> toPusherFunc=provider.getMenuInstance(Settings.OUTPUT,to,toSlot);
        for(int i=0;i<transSlot;++i){
            ItemPusher fromCache=fromPusherFunc.apply(i);//provider.get(Settings.INPUT,from,fromSlot[i]);
            ItemPusher toCache=toPusherFunc.apply(i);   //provider.get(Settings.OUTPUT,to,toSlot[i]);
            if(fromCache==null||(inbwlist(blacklist,fromCache)^whitlist)){
                continue;
            }
            if(toCache.getItem()==null){
                toCache.setFrom(fromCache);
                translimit=toCache.transportFrom(fromCache,translimit);
            }
            else if(isnull||toCache.getAmount()>=toCache.getMaxStackCnt()){
            }
            else if(CraftUtils.matchItemCounter(fromCache,toCache,true)){
                translimit=toCache.transportFrom(fromCache,translimit);
            }
            boolean hasUpdate=toCache.isDirty();
            if(hasUpdate){
                toCache.updateMenu(to);
                fromCache.updateMenu(from);
                if(lazy){
                    break;
                }
            }
            if(translimit<=0){
                break ;
            }
        }

    }
    /**
     * transportation from slots to cache ,will not update cache Amount to menu,will update slots to menu
     * @param menu
     * @param slots
     * @param provider
     * @return
     */
    public static ItemPusher[] transportItemToCache(BlockMenu cacheInv,ItemPusher[] cachelst, BlockMenu menu, int[] slots, ItemPusherProvider provider){
        int len=slots.length;
        ItemPusher tarCounter2;
        ItemPusher[] tarCounter=new ItemPusher[len];
        int len2=cachelst.length;
        IntFunction<ItemPusher> fromPusherFunc=provider.getMenuInstance(Settings.INPUT,menu,slots);
        for(int j=0;j<len;++j){
            tarCounter[j]=fromPusherFunc.apply(j);  //provider.get(Settings.INPUT,menu,slots[j]);
        }
        for(int j=0;j<len2;++j){
            ItemPusher cache=cachelst[j];
            if(cache==null){
                continue;
            }
            for(int i=0;i<len;i++){

                //quick pass
                if(tarCounter[i] != null){
                    tarCounter2=tarCounter[i];
                    if(cache.getItem()==null){
                        cache.setFrom(tarCounter2);
                        cache.grab(tarCounter2);
                        //立刻设置种类,防止设置源被同步
                        cache.updateMenu(cacheInv);
                    }
                    else if(CraftUtils.matchItemCounter(tarCounter2,cache,true)){
                        cache.grab(tarCounter2);
                    }
                }
            }
        }
        //
        updatePushers(tarCounter,menu);
        return tarCounter;
    }
    public static void updatePushers(ItemPusher[] pusher,BlockMenu menu){
        int len=pusher.length;
        ItemPusher cache;
        for(int i=0;i<len;++i){
            cache=pusher[i];
            if(cache!=null){
                cache.updateMenu(menu);
            }
        }
    }
}
