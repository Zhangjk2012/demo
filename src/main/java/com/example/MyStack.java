package com.example;
// Java program to implement a stack that supports
// getMinimum() in O(1) time and O(1) extra space.

import java.util.*;

// A user defined stack that supports getMin() in
// addition to push() and pop()
public class MyStack {
    Stack<Integer> s;
    Integer minEle;
    
    // Constructor
    MyStack() {
        s = new Stack<>();
    }
    
    // Prints minimum element of MyStack
    void getMin() {
        // Get the minimum number in the entire stack
        if (s.isEmpty())
            System.out.println("Stack is empty");
            
            // variable minEle stores the minimum element
            // in the stack.
        else
            System.out.println("Minimum Element in the " +
                    " stack is: " + minEle);
    }
    
    // prints top element of MyStack
    void peek() {
        if (s.isEmpty()) {
            System.out.println("Stack is empty ");
            return;
        }
        
        Integer t = s.peek(); // Top element.
        
        System.out.print("Top Most Element is: ");
        
        // If t < minEle means minEle stores
        // value of t.
        if (t < minEle)
            System.out.println(minEle);
        else
            System.out.println(t);
    }
    
    // Removes the top element from MyStack
    void pop() {
        if (s.isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }
        
        System.out.print("Top Most Element Removed: ");
        Integer t = s.pop();
        
        // Minimum will change as the minimum element
        // of the stack is being removed.
        if (t < minEle) {
            System.out.println(minEle);
            minEle = minEle - t;
        } else
            System.out.println(t);
    }
    
    // Insert new number into MyStack
    void push(Integer x) {
        if (s.isEmpty()) {
            minEle = x;
            s.push(x);
            System.out.println("Number Inserted: " + x);
            return;
        }
        
        // If new number is less than original minEle
        if (x < minEle) {
            s.push(x - minEle);
            minEle = x;
        } else
            s.push(x);
        
        System.out.println("Number Inserted: " + x);
    }
    
    public static void main(String[] args) {
//        MyStack s = new MyStack();
//        s.push(3);
//        s.push(5);
//        s.getMin();
//        s.push(2);
//        s.push(1);
//        s.getMin();
//        s.pop();
//        s.getMin();
//        s.pop();
//        s.peek();
        
        
        int t = 22345;
        double k = Math.log10(t);
        
        k = k - Math.floor(k);
        int x = (int) Math.pow(10, k);
        // 求最高位数
        System.out.println(x);
        // 求长度。
        System.out.println(Math.floor(Math.log10(t)) + 1);
    
        System.out.println(isPowerOfTwo(6));
    }
    
    /* Method to check if x is power of 2*/
    static boolean isPowerOfTwo(int x) {
     /* First x in the below expression is
     for the case when x is 0 */
        return x != 0 && ((x & (x - 1)) == 0);
    }
}
