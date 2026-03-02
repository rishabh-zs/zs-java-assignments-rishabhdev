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
        try {
            LocalDate sinceDate = LocalDate.parse(dateStr);
            List<Commit> commits = service.parseGitLog(filePath);

            System.out.println("Total Commits: " + service.getTotalCommitsPerDev(commits, sinceDate));
            System.out.println("Considering Active Developers since: " + sinceDate);
            System.out.println("Active Developers (without 2-day gaps): " + service.getActiveDevelopers(commits));
            System.out.println("InActive Developers (with 2-day gays): " + service.getInactiveDevelopers(commits));

        } catch (Exception e) {
            System.err.println("Error processing git log: " + e.getMessage());
        } finally {
            System.out.println("-----Finished processing git log--------");
        }
    }
}