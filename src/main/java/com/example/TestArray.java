package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by ZJK on 2018/3/7.
 */
public class TestArray {
    public static void main(String[] args) {
        
//        int[] a = {7,3,2,1,5,7,8,9,12,14};
        int[] a = {1,2,3,4,5,6,7,8};
    
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(()->{
            for (int i =0; i < a.length - 1; i++) {
                if (a[i] < a[i + 1]) {
                    System.out.println("wan cheng!");
                    return a[i];
                } else if (i == a.length - 2) {
                    System.out.println("wan cheng    !");
                    return a[a.length - 1];
                }
            }
            return 0;
        });
    
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(()->{
            for (int i = a.length - 1; i > 0; i--) {
                if (a[i] < a[i - 1]) {
                    System.out.println("wan cheng123!");
                    return a[i];
                } else if (i == 1) {
                    System.out.println("wan cheng12345!");
                    return a[0];
                }
            }
            return 0;
        });
    
        CompletableFuture<Object> future3 = CompletableFuture.anyOf(future1, future2);
        try {
            Object result = future3.get();
            System.out.println(result);
            
            
            int r = getMin(a, 0, a.length - 1);
            System.out.println(r + "   =====");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    public static int getMin(int[] a, int start, int end) {
        if (start == end) {
            return a[start];
        }
        int mid = (start + end) >> 1;
        if (mid == start) return a[start] < a[end] ? a[start] : a[end];
        if (a[mid] < a[mid + 1] && a[mid] < a[mid - 1]) {
            return a[mid];
        } else if (a[mid] > a[mid - 1]) {
            return getMin(a, start, mid - 1);
        } else if (a[mid] > a[mid + 1]) {
            return getMin(a, mid + 1, end);
        }
        return 0;
    }
}
