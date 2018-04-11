package com.example.fp;

/**
 * Created by ZJK on 2018/1/9.
 */
public class Consumer3Test {

    public void test(Consumer3<String, String, String> consumer3) {
        consumer3.accept("123", "456", "789");
    }

    public static void main(String[] args) {

        Consumer3Test test = new Consumer3Test();

        test.test((t1, t2, t3) -> {
            System.out.println(t1 + t2 + t3);
        });

    }

}
