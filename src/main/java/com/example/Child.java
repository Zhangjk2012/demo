package com.example;

public class Child implements Base {

	public int a = 123;

	public static void main(String[] args) {
		Child c = new Child();
        System.out.println(c.X);
	}
}
