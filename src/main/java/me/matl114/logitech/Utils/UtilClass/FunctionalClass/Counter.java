package me.matl114.logitech.Utils.UtilClass.FunctionalClass;

public class  Counter<T extends Object> {
    int a;
    T value;
    public Counter() {
        a = 0;
        value = null;
    }
    public Counter(int a) {
        this.a = a;
        value = null;
    }
    public Counter(int a,T value) {
        this.a = a;
        this.value = value;
    }
    public void decrement() {
        a-=1;
    }
    public int getCounter() {
        return a;
    }
    public T getValue() {
        return value;
    }
    public void increment() {
        a+=1;
    }
    public T read(int counterStamp){
        return read(counterStamp,1);
    }
    public T read(int counterStamp,int delayValue){
        if(this.a>=counterStamp-delayValue){
            return this.value;
        }else {
            this.value=null;
            return null;
        }
    }
    public void setCounter(int value) {
        a=value;
    }
    public void setValue(T value) {
        this.value = value;
    }
    public void updateValue(T value,int counterStamp) {
        this.value=value;
        this.a=counterStamp;
    }

}
