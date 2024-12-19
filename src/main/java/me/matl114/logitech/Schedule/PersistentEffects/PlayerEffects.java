package me.matl114.logitech.Schedule.PersistentEffects;

import java.util.function.Function;

import org.bukkit.entity.Player;

import me.matl114.logitech.Schedule.ScheduleEffects;

public class PlayerEffects {
    public static void grantEffect(AbstractEffect type, Player p, int level, int time){
        PlayerEffects eff = new PlayerEffects(type,time,level);
        ScheduleEffects.addEffect(p,eff);
    }
    public static void grantEffect(AbstractEffect type, Player p, int level, int time, Function<Player,Boolean> predicate){
        PlayerEffects eff = new PlayerEffects(type,time,level);
        ScheduleEffects.addEffect(p,eff,predicate);
    }
    public final AbstractEffect TYPE;
    public int duration;
    public int level;
    public boolean pendingMove;
    public PlayerEffects(AbstractEffect type,int time,int level) {
        this.TYPE = type;
        this.duration = time;
        this.level = level;
        this.pendingMove = false;
    }
    public void end(Player p){
        TYPE.removeEffect(p,level);
        this.pendingMove = true;
    }
    public void extend(PlayerEffects eff){

        this.duration=eff.duration;
        this.level=eff.level;
    }
    public final void finish(){
        this.duration=0;
    }
    public String getEffectId(){
        return TYPE.getEffectId();
    }
    public int getLeftTime(){
        return duration;
    }
    public AbstractEffect getType(){
        return TYPE;
    }
    public final void halt(){
        this.pendingMove=true;
    }
    public final boolean isFinished(){
        return duration <= 0 || this.pendingMove;
    }
    public boolean isPendingMove(){
        return pendingMove;
    }
    public void start(Player p){
        TYPE.aquireEffect(p,level);
    }
    public void tick(Player p){

        duration--;
        TYPE.tickEffect(p,level);
    }

}
