package com.zs.assignment5;

import com.zs.assignment5.controllers.GitLogController;

public class Main {
    public static void main(String[] args) {
        // Using default values if no command-line arguments are provided
        String filePath = (args.length > 0) ? args[0] : "src/packages/sample_git.log";
        String dateStr = (args.length > 1) ? args[1] : "2026-02-20";

        GitLogController controller = new GitLogController();
        controller.processGitStats(filePath, dateStr);
    }
}