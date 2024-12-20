package me.matl114.logitech.Utils.UtilClass.MultiBlockClass;

import org.bukkit.util.Vector;

public class MultiBlock implements AbstractMultiBlock{
    private final AbstractMultiBlockType TYPE;
    private final MultiBlockService.Direction DIRECTION;
    public MultiBlock(AbstractMultiBlockType type, MultiBlockService.Direction direction) {
        this.TYPE = type;
        this.DIRECTION = direction;
    }
    @Override
	public MultiBlockService.Direction getDirection() {
        return DIRECTION;
    }

    @Override
	public Vector getStructurePart(int index){
        return DIRECTION.rotate( getType().getSchemaPart(index));
    }
    @Override
	public AbstractMultiBlockType getType() {
        return TYPE;
    }
}
