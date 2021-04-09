package kz.attractor.java;

import kz.attractor.java.lesson44.Lesson44Server;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            int port = 9899;
            new Lesson44Server("localhost", port).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
