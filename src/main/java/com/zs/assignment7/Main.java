package com.zs.assignment7;

import com.zs.assignment7.controllers.DatabaseController;

/**
 * Main class to run the application.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        DatabaseController dbController = new DatabaseController();
        dbController.executeFlow();
    }
}
