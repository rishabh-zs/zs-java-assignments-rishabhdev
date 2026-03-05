package com.zs.assignment5;

import com.zs.assignment5.controllers.GitLogController;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------- GIT LOG ANALYZER -----------");
        GitLogController controller = new GitLogController();
        controller.start(args);
    }
}