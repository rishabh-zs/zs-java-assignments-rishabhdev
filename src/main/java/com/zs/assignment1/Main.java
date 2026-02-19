package com.zs.assignment1;

import com.zs.assignment1.controllers.Controller;

public class Main {
    public static void main(String[] args) {
        System.out.println("starting point");

        Controller app = new Controller();
        app.executeRequest();
    }
}