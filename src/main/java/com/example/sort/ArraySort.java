package com.example.sort;

/**
 * Created by ZJK on 2018/2/26.
 */
public class ArraySort {
    
    public static void main(String[] args) {
    
        int[] a = {2,3,6,5,4,23,10,8,7};
    
        simpleSort(a);
        
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
    
    /**
     * 直接插入排序，时间复杂度是：o(n^2)
     * @param a
     */
    public static void straightInsertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0 && a[j] < a[j - 1]; j--) {
                // 如果小，则直接换
                int tmp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = tmp;
            }
        }
    }
    
    /**
     * 希尔排序。
     * 在插入排序的基础上得来。
     * 总的来说：一个增量D，然后每个增量相比较，最后，D变为1，使其成为插入排序。
     * @param a
     */
    public static void shellSort(int[] a) {
        for (int d = a.length >> 1; d > 0; d = d >> 1) {
            for (int i = d; i < a.length; i++) {
                for (int j = i; j - d > 0 && a[j] < a[j - d]; j = j - d) {
                    swap(a,j,j-d);
                }
            }
        }
    }
    
    /**
     * 简单排序。记录最小值的下标，然后再置换
     * @param a
     */
    public static void simpleSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[min] > a[j]) min = j;
            }
            if (min != i) {
                swap(a, i, min);
            }
        }
    }
    
    /**
     * 堆排序
     * @param a
     */
    public static void heapSort(int[] a) {
    
    
    
    }
    
    /**
     * 交换数组元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int[] arr, int a, int b) {
        arr[a] = arr[a] + arr[b];
        arr[b] = arr[a] - arr[b];
        arr[a] = arr[a] - arr[b];
    }
    
}
