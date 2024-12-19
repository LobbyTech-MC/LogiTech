package me.matl114.logitech.Utils.Algorithms;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

public class DynamicMapper<T,W>  extends AbstractList<T> implements List<T>{
    public W[] data;
    protected T[] result;
    protected Function<W,T> mapper;
    protected int size;
    public DynamicMapper(W[] data, Function<W,T> mapper,T[] result) {
        this.data = data;
        this.size = data.length;
        this.result = result;
        this.mapper=mapper;
    }
    @Override
	public T get(int index){
        if(index<0 || index>=size){
            throw new IndexOutOfBoundsException();
        }else {
            if(result[index]==null){
                result[index]=mapper.apply(data[index]);
            }
            return result[index];
        }
    }
    @Override
	public boolean isEmpty(){
        return size==0;
    }
    @Override
	public int size(){
        return size;
    }

}
