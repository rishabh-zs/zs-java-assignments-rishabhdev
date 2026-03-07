package com.zs.assignment5;

import com.zs.assignment5.controllers.GitLogController;
import com.zs.assignment5.repositories.FileGitLogRepository;
import com.zs.assignment5.services.GitLogService;

/**
 * Application entry point for Assignment 5 (Git Log Analyzer).
 * <p>
 * This class initializes the CLI flow by creating {@link GitLogController}
 * and delegating argument handling and execution to it.
 */
public class Main {
    /**
     * Starts the Git Log Analyzer command-line application.
     *
     * @param args command-line arguments expected by the controller
     *             (for example, log file path and target date)
     */
    public static void main(String[] args) {
        System.out.println("---------- GIT LOG ANALYZER -----------");
        GitLogService service = new GitLogService(new FileGitLogRepository());
        GitLogController controller = new GitLogController(service);
        controller.start(args);
    }
}
