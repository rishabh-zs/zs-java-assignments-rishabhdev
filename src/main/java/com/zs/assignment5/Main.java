package com.zs.assignment5;

import com.zs.assignment5.controllers.GitLogController;

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
        String filePath = (args.length > 0) ? args[0] : "src/packages/sample_git.log";
        String dateStr = (args.length > 1) ? args[1] : "2026-02-20";

        GitLogController controller = new GitLogController();
        controller.processGitStats(filePath, dateStr);
    }
}