package com.zs.assignment4;

import com.zs.assignment4.controllers.CategoryHierarchyController;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("---LRU_CACHE_IMPLEMENTATION---");
        CategoryHierarchyController controller = new CategoryHierarchyController();
        controller.start();
    }
}