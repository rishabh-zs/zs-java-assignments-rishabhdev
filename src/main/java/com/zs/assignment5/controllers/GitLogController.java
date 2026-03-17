package com.zs.assignment5.controllers;

import com.zs.assignment5.exceptions.GitLogException;
import com.zs.assignment5.models.Commit;
import com.zs.assignment5.services.GitLogService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * GitLogController serves as the main entry point for the application, handling user interaction and orchestrating the processing of Git log data.
 */
public class GitLogController {

    private final GitLogService gitLogService;

    /**
     * Instantiates a new Git log controller.
     *
     * @param gitLogService service used to parse and analyze Git log data
     */
    public GitLogController(GitLogService gitLogService) {
        this.gitLogService = gitLogService;
    }

    /**
     * start the application, either using command-line arguments or interactive prompts.
     *
     * @param args the args
     */
    public void start(String[] args) {
        String filePath = "";
        LocalDate d = null;

        if (args.length >= 2) {
            filePath = args[0];
            try {
                d = LocalDate.parse(args[1]);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format in arguments. Expected YYYY-MM-DD.");
                return;
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Git log file path (e.g., src/main/resources/log.txt): ");
            filePath = scanner.nextLine().trim();

            System.out.print("Enter starting date 'd' (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                d = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                return;
            } finally {
                scanner.close();
            }
        }

        processGitLog(filePath, d);
    }

    private void processGitLog(String filePath, LocalDate sinceDate) {
        try {
            List<Commit> commits = parseCommits(filePath);
            printTotalCommits(commits, sinceDate);
            printDailyCommits(commits, sinceDate);
            printDevelopersWithInactivity(commits);
            LocalDate windowEnd = sinceDate.plusDays(2);
            printActiveDevelopers(commits, sinceDate, windowEnd);
            printInactiveDevelopers(commits, sinceDate, windowEnd);
        } catch (GitLogException e) {
            System.err.println("\n❌ Processing Error: " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
        }
    }

    private List<Commit> parseCommits(String filePath) throws GitLogException {
        System.out.println("\nParsing Git Log file: " + filePath);
        List<Commit> commits = gitLogService.getCommits(filePath);
        System.out.println("✅ Successfully parsed " + commits.size() + " commits.\n");
        return commits;
    }

    private void printTotalCommits(List<Commit> commits, LocalDate sinceDate) {
        System.out.println("--- 1. Total Commits by Developer Since " + sinceDate + " ---");
        Map<String, Long> totalCommits = gitLogService.getTotalCommitsSince(commits, sinceDate);
        if (totalCommits.isEmpty()) System.out.println("No commits found since this date.");
        totalCommits.forEach((dev, count) -> System.out.println(dev + " : " + count + " commits"));
    }

    private void printDailyCommits(List<Commit> commits, LocalDate sinceDate) {
        System.out.println("\n--- 2. Daily Commits by Developer Since " + sinceDate + " ---");
        Map<String, Map<LocalDate, Long>> dailyCommits = gitLogService.getDailyCommitsSince(commits, sinceDate);
        if (dailyCommits.isEmpty()) System.out.println("No commits found since this date.");
        dailyCommits.forEach((dev, dates) -> {
            System.out.println("Developer: " + dev);
            dates.forEach((date, count) -> System.out.println("  " + date + " -> " + count + " commits"));
        });
    }

    private void printDevelopersWithInactivity(List<Commit> commits) {
        System.out.println("\n--- 3. Developers with >2 Successive Days of No Commits (Overall) ---");
        List<String> devsWithGaps = gitLogService.getDevelopersWithInactivity(commits);
        if (devsWithGaps.isEmpty()) {
            System.out.println("All developers have been active without a 2-day gap.");
        } else {
            devsWithGaps.forEach(dev -> System.out.println("- " + dev));
        }
    }

    private void printActiveDevelopers(List<Commit> commits, LocalDate sinceDate, LocalDate windowEnd) {
        System.out.println("\n--- 4. Active Developers (At least 1 commit between " + sinceDate + " and " + windowEnd + ") ---");
        Set<String> activeDevs = gitLogService.getActiveDevelopers(commits, sinceDate);
        if (activeDevs.isEmpty()) {
            System.out.println("No developers were active during this 2-day period.");
        } else {
            activeDevs.forEach(dev -> System.out.println("- " + dev));
        }
    }

    private void printInactiveDevelopers(List<Commit> commits, LocalDate sinceDate, LocalDate windowEnd) {
        System.out.println("\n--- 5. InActive Developers (0 commits between " + sinceDate + " and " + windowEnd + ") ---");
        Set<String> inactiveDevs = gitLogService.getInactiveDevelopers(commits, sinceDate);
        if (inactiveDevs.isEmpty()) {
            System.out.println("All historical developers were active during this 2-day period!");
        } else {
            inactiveDevs.forEach(dev -> System.out.println("- " + dev));
        }
    }
}
