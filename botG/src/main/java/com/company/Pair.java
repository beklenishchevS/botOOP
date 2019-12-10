package com.company;

public class Pair<T, T1> {
    private T value;
    private T1 value1;
    public Pair(T value, T1 value1){
        this.value = value;
        this.value1 = value1;
    }

    public T getKey()
    {
        return value;
    }

    public T1 getValue()
    {
        return value1;
    }
}
