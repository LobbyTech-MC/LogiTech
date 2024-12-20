package me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiLevelBlock;

import java.util.List;

import org.bukkit.util.Vector;

import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.AbstractMultiBlock;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;

public class MultiLevelBlock implements AbstractMultiBlock {
    protected int level;
    protected int maxLevel;
    protected AbstractMultiBlock[] SUBPARTS;
    protected int[] INDEXS;
    protected int[] SIZES;
    protected MultiLevelBlockType schema;
    protected MultiBlockService.Direction direction;
    public MultiLevelBlock(MultiLevelBlockType type, int level, MultiBlockService.Direction direction, List< AbstractMultiBlock> multilist){
        this.SUBPARTS=multilist.toArray(new AbstractMultiBlock[level]);
        this.schema=type;
        this.level=level;
        this.maxLevel=type.SUB_NUM;
        this.direction=direction;
        this.INDEXS=new int[level+1];
        this.INDEXS[0]=0;
        this.SIZES=new int[level];
        for (int i=0;i<level;++i){
            this.SIZES[i]=this.SUBPARTS[i].getStructureSize();
            this.INDEXS[i+1]=this.INDEXS[i]+this.SIZES[i];
        }
    }
    @Override
	public MultiBlockService.Direction getDirection() {
        return direction;
    }
    public int getLevel(){
        return level;
    }
    /**
     * get structure i th block, should be rotated by DIRECTION
     * @param index
     * @return
     */
   @Override
public Vector getStructurePart(int index){
       for(int i=0;i<level;++i){
           if(index<INDEXS[i+1]){
               return SUBPARTS[i].getStructurePart(index-INDEXS[i]);
           }
       }
       return null;
    }

    /**
     * get structure i th id
     * @param index
     * @return
     */
    @Override
	public String getStructurePartId(int index){
        for(int i=0;i<level;++i){
            if(index<INDEXS[i+1]){
                return SUBPARTS[i].getStructurePartId(index-INDEXS[i]);
            }
        }
        return null;
    }

    /**
     * get structure size
     * @return
     */
    @Override
	public int getStructureSize(){
        return INDEXS[level];
    }

    @Override
	public MultiLevelBlockType getType(){
        return this.schema;
    }
}
