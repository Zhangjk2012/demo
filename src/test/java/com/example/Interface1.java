package com.example;

/**
 * Created by ZJK on 2017/9/19.
 */
public interface Interface1 {

    static void staticMethod() {
        System.out.println("staticMethod1");
    }

    default void defaultMethod() {
        System.out.println("defaultMethod1");
    }

}
