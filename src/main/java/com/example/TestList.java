package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by ZJK on 2017/9/19.
 */
public class TestList {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>(100000);
        
        List<? super Number> numbers = new ArrayList<>(12);
        numbers.add(1);
        numbers.add(1.0d);
        
        List<? extends Number> numbers1 = new ArrayList<>(12);
        Number n = numbers1.get(0);
        
        
        

//        Random random = new Random();
//
//        for (int i = 0; i < 1000000; i++) {
//            list.add(random.nextInt(20000));
//        }
//
//        System.out.println(System.currentTimeMillis());
//        System.out.println(list.size());
//        List<Integer> l = list.stream().distinct().collect(Collectors.toList());
//        System.out.println(l.size());
//        System.out.println(System.currentTimeMillis());


    }

}
