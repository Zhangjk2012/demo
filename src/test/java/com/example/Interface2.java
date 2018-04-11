package com.example;

/**
 * Created by ZJK on 2017/9/19.
 */
public interface Interface2 {

    static void staticMethod() {
        System.out.println("staticMethod2");
    }

    default void defaultMethod() {
        System.out.println("defaultMethod2");
    }

}
