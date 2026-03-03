package com.zs.assignment5.services;

import com.zs.assignment5.models.Commit;
import com.zs.assignment5.exceptions.*;
import com.zs.assignment5.annotations.ParserInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Git log service.
 */
@ParserInfo(version = "2.0")
public class GitLogService {
    /**
     * Parse git log list.
     *
     * @param filePath the file path
     * @return the list
     * @throws GitLogException the git log exception
     * @throws IOException     the io exception
     */
    public List<Commit> parseGitLog(String filePath) throws GitLogException, IOException {
        File file = new File(filePath);
        if (!file.exists()){
            throw new FileNotFoundException("Log file not found at: " + filePath);
        }

        List<Commit> commits = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (!line.startsWith("commit")) {
                    throw new FileFormatEntryException("Invalid Format: Expected 'commit' but found: " + line);
                }

                String authorLine = br.readLine();
                if (authorLine == null || !authorLine.startsWith("Author:")) {
                    throw new GitLogException("Incomplete info: Missing Author line");
                }

                String dateLine = br.readLine();
                if (dateLine == null || !dateLine.startsWith("Date:")) {
                    throw new GitLogException("Incomplete info: Missing Date line");
                }

                LocalDate date;
                try {
                    date = extractDate(dateLine);
                } catch (GitLogException e) {
                    br.readLine();
                    continue;
                }

                String developer = extractDeveloperName(authorLine);

                String message = br.readLine();
                if (message == null) {
                    message = "";
                }

                commits.add(new Commit(developer, date, message));
            }
        }

        return commits;
    }

    private String extractDeveloperName(String authorLine) {
        int startIdx = authorLine.indexOf(": ") + 2;
        int endIdx = authorLine.indexOf(" <");
        if (endIdx > startIdx) {
            return authorLine.substring(startIdx, endIdx);
        }
        return authorLine;
    }

    private LocalDate extractDate(String dateLine) throws GitLogException {
        String dateStr = dateLine.substring(5).trim();

        if (dateStr.isEmpty()) {
            throw new GitLogException("Date is missing");
        }

        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            throw new GitLogException("Invalid date format: " + dateStr);
        }
    }

    /**
     * Gets total commits per dev.
     *
     * @param commits the commits
     * @param since   the since
     * @return the total commits per dev
     */
    public Map<String, Long> getTotalCommitsPerDev(List<Commit> commits, LocalDate since) {
        return commits
                .stream()
                .filter(c -> !c.date().isBefore(since))
                .collect(Collectors.groupingBy(Commit::developer, Collectors.counting()));
    }

    /**
     * Gets daily commits per developer since the provided date.
     *
     * @param commits the commits
     * @param since   the since date (inclusive)
     * @return map of developer -> (date -> commit count)
     */
    public Map<String, Map<LocalDate, Long>> getDailyCommitsPerDevSince(List<Commit> commits, LocalDate since) {
        return commits.stream()
                .filter(c -> !c.date().isBefore(since))
                .collect(Collectors.groupingBy(
                        Commit::developer,
                        TreeMap::new,
                        Collectors.groupingBy(
                                Commit::date,
                                TreeMap::new,
                                Collectors.counting()
                        )));
    }

    /**
     * Gets developers who have at least one 2-day consecutive no-commit streak
     * within the provided date range [fromDate, toDate].
     *
     * @param commits   the commits
     * @param fromDate  start date (inclusive)
     * @param toDate    end date (inclusive)
     * @return list of developers with a 2-day no-commit streak
     */
    public List<String> getDevelopersWithTwoDayNoCommitGap(List<Commit> commits, LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate cannot be after toDate");
        }

        Map<String, Set<LocalDate>> commitsByDevByDate = commits.stream()
                .collect(Collectors.groupingBy(
                        Commit::developer,
                        Collectors.mapping(Commit::date, Collectors.toSet())));

        List<String> developersWithGap = new ArrayList<>();

        for (Map.Entry<String, Set<LocalDate>> entry : commitsByDevByDate.entrySet()) {
            Set<LocalDate> commitDatesInRange = entry.getValue().stream()
                    .filter(d -> !d.isBefore(fromDate) && !d.isAfter(toDate))
                    .collect(Collectors.toSet());

            int missingStreak = 0;
            LocalDate current = fromDate;
            while (!current.isAfter(toDate)) {
                if (commitDatesInRange.contains(current)) {
                    missingStreak = 0;
                } else {
                    missingStreak++;
                    if (missingStreak >= 2) {
                        developersWithGap.add(entry.getKey());
                        break;
                    }
                }
                current = current.plusDays(1);
            }
        }

        Collections.sort(developersWithGap);
        return developersWithGap;
    }

    /**
     * Gets inactive developers.
     *
     * @param commits the commits
     * @return the inactive developers
     */
    public List<String> getInactiveDevelopers(List<Commit> commits) {
        Map<String, List<Commit>> commitsByDev = commits
                .stream()
                .collect(Collectors.groupingBy(Commit::developer));

        List<String> inactiveDevelopers = new ArrayList<>();

        for (Map.Entry<String, List<Commit>> entry : commitsByDev.entrySet()) {
            List<LocalDate> dates = entry.getValue()
                    .stream()
                    .map(Commit::date)
                    .sorted()
                    .toList();

            for (int i = 0; i < dates.size() - 1; i++) {
                long daysBetween = ChronoUnit.DAYS.between(dates.get(i), dates.get(i + 1));
                if (daysBetween >= 2) {
                    inactiveDevelopers.add(entry.getKey());
                    break;
                }
            }
        }

        return inactiveDevelopers;
    }

    /**
     * Gets active developers.
     *
     * @param commits the commits
     * @return the active developers
     */
    public List<String> getActiveDevelopers(List<Commit> commits) {
        Map<String, List<Commit>> commitsByDev = commits
                .stream()
                .collect(Collectors.groupingBy(Commit::developer));

        List<String> activeDevelopers = new ArrayList<>();

        for (Map.Entry<String, List<Commit>> entry : commitsByDev.entrySet()) {
            List<LocalDate> dates = entry.getValue().stream()
                    .map(Commit::date)
                    .sorted()
                    .toList();

            boolean hasGap = false;
            for (int i = 0; i < dates.size() - 1; i++) {
                long daysBetween = ChronoUnit.DAYS.between(dates.get(i), dates.get(i + 1));
                if (daysBetween >= 2) {
                    hasGap = true;
                    break;
                }
            }

            if (!hasGap) {
                activeDevelopers.add(entry.getKey());
            }
        }

        return activeDevelopers;
    }
}
