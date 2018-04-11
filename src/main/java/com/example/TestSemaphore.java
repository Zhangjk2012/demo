package com.example;

import java.util.concurrent.Semaphore;

/**
 * Created by ZJK on 2018/3/8.
 */
public class TestSemaphore {
    public static void main(String[] args) {
    
        Semaphore semaphore = new Semaphore(1);
        try {
            semaphore.acquire();
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
        semaphore.release();
    
        try {
            semaphore.acquire();
            semaphore.acquire();
            semaphore.acquire();
            semaphore.acquire();
            System.out.println("==================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
    
    }
}
