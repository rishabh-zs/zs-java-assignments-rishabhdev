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
        System.out.println("-----STARTING GIT LOG ANALYSIS--------");
        System.out.println();
        String filePath = (args.length > 0) ? args[0] : "src/packages/sample_git.log";
        String dateStr = (args.length > 1) ? args[1] : "2026-02-20";
        String fromDateStr = (args.length > 2) ? args[2] : dateStr;
        String toDateStr = (args.length > 3) ? args[3] : dateStr;

        GitLogController controller = new GitLogController();
        if (args.length > 2) {
            controller.processGitStats(filePath, dateStr, fromDateStr, toDateStr);
        } else {
            controller.processGitStats(filePath, dateStr);
        }
    }
}
