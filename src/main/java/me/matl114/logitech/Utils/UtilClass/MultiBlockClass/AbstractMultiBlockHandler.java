package me.matl114.logitech.Utils.UtilClass.MultiBlockClass;

import org.bukkit.Location;

public interface AbstractMultiBlockHandler {
    /**
     * accept in core ticks,check if active and return boolean value,when active is down, return false,
     * the service method will call the  destroy method
     *  @return
     */
    public boolean acceptCoreRequest();
    /**
     * accept in part ticks
     * @param loc
     */
    public void acceptPartRequest(Location loc);
    //响应主核信号 返回是否还正常
    /**
     * strict check if the multiblock matches,not suggested in tickers
     * @return
     */
    public int checkIfComplete();
    /**
     * random check if the multiblock matches,suggested in tickers
     * @return
     */
    default int checkIfCompleteRandom(){
        return checkIfComplete();
    }
    /**
     * this method should called onMultiBlockDisable for CORE and reset blockdata!
     */
    public void destroy(MultiBlockService.DeleteCause cause);
    /**
     * return the index th block in structure
     * @param index
     * @return
     */
    public Location getBlockLoc(int index);

    /**
     * location of core
     * @return
     */
    public Location getCore() ;

    public MultiBlockService.Direction getDirection();

    public MultiBlockService.DeleteCause getLastDeleteCause();

    public AbstractMultiBlock getMultiBlock();

    /**
     * return the size of structure
     * @return
     */
    public int getSize();

    /**
     * get handler uid
     * @return
     */
    public String getUid();

    /**
     * is multiblock be build
     * @return
     */
    public boolean isActive();

    //响应部件信号
    public void toggleOff(MultiBlockService.DeleteCause cause);
}
