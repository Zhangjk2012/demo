package com.example;

/**
 * Created by ZJK on 2018/3/9.
 */
public class FibFind {
    public static void main(String[] args) {
        
        int arr[] = {10, 22, 35, 40, 45, 50, 80, 82, 85, 90, 100};
        int n = 11;
        int x = 101;
        System.out.print("Found at index: " + fibMonaccianSearch(arr, x, n));
    }
    
    public static int fibMonaccianSearch(int arr[], int x, int n) {
        int fibMMm2 = 0;
        int fibMMm1 = 1;
        int fibM = fibMMm2 + fibMMm1;
        while (fibM < n) {
            fibMMm2 = fibMMm1;
            fibMMm1 = fibM;
            fibM = fibMMm2 + fibMMm1;
        }
        int offset = 0;
        while (fibM > 1) {
            int i = min(offset + fibMMm2, n - 1);
            if (arr[i] < x) {
                fibM = fibMMm1;
                fibMMm1 = fibMMm2;
                fibMMm2 = fibM - fibMMm1;
                offset = i;
            } else if (arr[i] > x) {
                fibM = fibMMm2;
                fibMMm1 = fibMMm1 - fibMMm2;
                fibMMm2 = fibM - fibMMm1;
            } else return i;
        }
        
        if (fibMMm1 == 1 && offset + 1 <= n - 1 && arr[offset + 1] == x)
            return offset + 1;
        return -1;
    }
    
    public static int min(int x, int y) {
        return (x <= y) ? x : y;
    }
}
