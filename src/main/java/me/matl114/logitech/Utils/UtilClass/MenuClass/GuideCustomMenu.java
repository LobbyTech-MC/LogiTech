package me.matl114.logitech.Utils.UtilClass.MenuClass;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * CustomMenu that can record history
 */
public class GuideCustomMenu extends CustomMenu {
    private boolean useHistory;

    private PlayerHistoryRecord<CustomMenu> history;
    public GuideCustomMenu(String title,int size, int menulens,MenuFactory factory){
        super(title,size,menulens,factory);
        this.useHistory=true;
        this.backHandlers=((player, i, itemStack, clickAction) -> {
            if(this.useHistory&&this.history!=null){
                history.deleteRecord(player,this);
            }
            return ChestMenuUtils.getEmptyClickHandler().onClick(player,i,itemStack,clickAction);
        });
    }
    @Override
	public void openPages(Player p,int page){
        if(useHistory&&history!= null){
//            CustomMenu menu= history.getRecord(p);
//            if(menu!=null){
//                ChestMenu historyMenu=menu.constructPage(menu.lastpage);
//                historyMenu.open(p);
//                return;
//            }
            history.addRecord(p,this);
        }
        ChestMenu menu=constructPage(page);
        menu.open(p);
    }
    @Override
	public CustomMenu setBackHandler(ChestMenu.MenuClickHandler handler){
        ChestMenu.MenuClickHandler han =handler==null? ChestMenuUtils.getEmptyClickHandler():handler;
        this.backHandlers=((player, i, itemStack, clickAction) -> {
            if(this.useHistory&&this.history!=null){
                history.deleteRecord(player,this);
            }
            return han.onClick(player,i,itemStack,clickAction);
        });
        return this;
    }
    public GuideCustomMenu setHistory(PlayerHistoryRecord<CustomMenu> history){
        this.history=history;
        return this;
    }
    public CustomMenu setUseHistory(boolean useHistory){
        this.useHistory = useHistory;
        return this;
    }
}
