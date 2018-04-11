package com.example;

/**
 * Created by ZJK on 2017/9/19.
 */
public class InterfaceImpl implements Interface1, Interface2 {

    /**
     * 之所以要重写默认方法，是因为：静态方法，只能通过类名来调用，且子类不继承。
     * 默认方法，子类可以继承，方法同名后，如果不重写，则就不知道调用的是哪个方法了。
     */
    public void defaultMethod() {
        System.out.println("InterfaceImpl");
        Interface1.super.defaultMethod();
    }
}
