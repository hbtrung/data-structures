package hw3.hash;

import edu.princeton.cs.algs4.StdRandom;

public class Hint {
    public static void main(String[] args) {
        System.out.println("The powers of 256 in Java are:");
        int x = 0;
        int num = StdRandom.uniform(0, 255);
        for (int i = 0; i < 6; i += 1) {
            System.out.println(i + "th power: " + x);
            x = x * 256;
            x += num;
        }
        for (int i = 6; i < 10; i++) {
            System.out.println(i + "th power: " + x);
            x = x * 256;
            x += 0;
        }
    }
} 
