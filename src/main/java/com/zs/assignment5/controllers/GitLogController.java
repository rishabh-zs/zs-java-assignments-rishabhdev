package com.zs.assignment5.controllers;

import com.zs.assignment5.services.GitLogService;
import com.zs.assignment5.models.Commit;
import java.time.LocalDate;
import java.util.List;

/**
 * The type Git log controller.
 */
public class GitLogController {
    private final GitLogService service = new GitLogService();

    /**
     * Process git stats.
     *
     * @param filePath the file path
     * @param dateStr  the date str
     */
    public void processGitStats(String filePath, String dateStr) {
        processGitStats(filePath, dateStr, dateStr, dateStr);
    }

    /**
     * Process git stats.
     *
     * @param filePath    the file path
     * @param dateStr     the since date for commit counting
     * @param fromDateStr the start date for inactivity analysis
     * @param toDateStr   the end date for inactivity analysis
     */
    public void processGitStats(String filePath, String dateStr, String fromDateStr, String toDateStr) {
        try {
            LocalDate sinceDate = LocalDate.parse(dateStr);
            LocalDate fromDate = LocalDate.parse(fromDateStr);
            LocalDate toDate = LocalDate.parse(toDateStr);
            List<Commit> commits = service.parseGitLog(filePath);

            System.out.println("Total Commits: " + service.getTotalCommitsPerDev(commits, sinceDate));
            System.out.println("Daily Commits per Developer since " + sinceDate + ": "
                    + service.getDailyCommitsPerDevSince(commits, sinceDate));
            System.out.println("Developers with 2-day no-commit streak between " + fromDate + " and " + toDate + ": "
                    + service.getDevelopersWithTwoDayNoCommitGap(commits, fromDate, toDate));
            System.out.println("Considering Active Developers since: " + sinceDate);
            System.out.println("Active Developers (without 2-day gaps): " + service.getActiveDevelopers(commits));
            System.out.println("InActive Developers (with 2-day gays): " + service.getInactiveDevelopers(commits));

        } catch (Exception e) {
            System.err.println("Error processing git log: " + e.getMessage());
        } finally {
            System.out.println();
            System.out.println("-----FINISHED PROCESSING GIT LOG--------");
        }
    }
}
