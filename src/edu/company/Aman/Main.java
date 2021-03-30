package edu.company.Aman;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new mainServer("localhost",8787).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
