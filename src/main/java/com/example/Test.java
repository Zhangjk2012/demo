package com.example;

import com.alibaba.fastjson.JSONObject;
import com.typesafe.config.ConfigFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zhangjiangke on 2017/6/15.
 */
public class Test {

    private static int fib(int n, int pre, int acc) {
        if (n < 2) return acc;
        else return fib(n - 1, acc, acc + pre);
    }

    static class Node {
        public Node next = null;
        public int data;
        public Node(int data) {
            this.data = data;
        }
    }



    public static Node reverseNode(Node curr, Node next) {
        if (next == null) {
            return curr;
        } else {
            Node tmp = next.next;
            Node t1 = next;
            t1.next = curr;
            return reverseNode(t1, tmp);
        }
    }

    public static Node reverseNodeWhile(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node tmp = null;
        while (head != null) {
            Node t = new Node(head.data);
            t.next = tmp;
            tmp = t;
            head = head.next;
        }
        return tmp;
    }

    public static void main(String[] args) {

//        Node head1 = new Node(1);
//        Node head2 = new Node(2);
//        Node head3 = new Node(3);
//        Node head4 = new Node(4);
//
//        head1.next = head2;
//        head2.next = head3;
//        head3.next = head4;
//
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    
        LinkedHashMap<String, String> s = new LinkedHashMap<>(1);
        s.put("1","1");


//        Node r = reverseNode(null,head1);
//
//        System.out.println(r);

        int[] a1= {1,2,3,4,5,6};


//        System.out.println(fib(10, 1, 0));

//        ByteBuf buf = Unpooled.buffer(1024);
//        buf.writeLong(1L);
//        buf.writeByte(2);
//        buf.writeInt(3);
//        byte[] b = buf.array();
//        buf.clear();
//        buf.release();
//        System.out.println(b);


//        Map<String, String> map = new HashMap<>();
//
//        String key = "test";
//
//        String value = map.compute(key, (k, v) -> {
//
//            return v;
//        });

//        ConfigFactory.load();
//        String configFileName = "reference.conf";
//        try {
//            System.out.println(Thread.currentThread().getContextClassLoader().getResource(configFileName));
////            Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(configFileName);
////            while (e.hasMoreElements()) {
////                URL url = e.nextElement();
////                System.out.println(url);
////            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }


//        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//
//        numbers.stream().parallel().reduce(0, (total, e)-> total + e);


//        final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(16);
//
//        new Thread(()-> {
//            map.compute("1234", (k, v) -> {
//                System.out.println("==========");
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (v == null || "".equals(v)) {
//                    return "1234";
//                }
//                return v;
//            });
//
//        }).start();
//
//        new Thread(()-> {
//            map.compute("1234", (k, v) -> {
//                System.out.println("--------------");
//                if (v == null || "".equals(v)) {
//                    return "1234";
//                }
//                return v;
//            });
//
//        }).start();



    }

    public static Test t11 = new Test();
    public static Test t22 = new Test();

    public void test(Test t1, Test t2){
        System.out.println(t1 == t2);
        synchronized (t1) {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("963852741");
            synchronized (t2) {
                System.out.println("12345567");
            }
        }
    }
    public void test2(Test t1, Test t2){
        synchronized (t22) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("==========");
            synchronized (t11) {
                System.out.println("++++++++");
            }
        }
    }

}
