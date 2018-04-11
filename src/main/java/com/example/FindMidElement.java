package com.example;

/**
 * Created by ZJK on 2018/3/9.
 */
public class FindMidElement {
    public static void main(String[] args) {
    
        
        int a = 0, b = 1, c;
        for (int i = 2; i < 4; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        System.out.println(b);
        
        
        
        
        
        
        Node first = new Node(1);
        Node second = new Node(2);
        Node third = new Node(3);
        Node forth = new Node(4);
        Node fifth = new Node(5);
        first.next = second;
        second.next = third;
        third.next = forth;
        forth.next = fifth;
        
        
        Node fast = first;
        Node slow = first;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
    
        System.out.println(slow.data);
        
        
        Node mid = first;
        int counter = 0;
        while (first.next != null) {
            if (counter % 2 == 1) {
                mid = mid.next;
            }
            counter ++;
            first = first.next;
        }
        System.out.println(mid.data);
        
    }
    
    static class Node {
        public Node next = null;
        public int data;
        public Node(int data) {
            this.data = data;
        }
    }
}
