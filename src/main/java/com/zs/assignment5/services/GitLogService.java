package com.zs.assignment5.services;

import com.zs.assignment5.models.Commit;
import com.zs.assignment5.exceptions.*;
import com.zs.assignment5.annotations.ParserInfo;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@ParserInfo(version = "2.0")
public class GitLogService {

    // Generic method to read and parse
    public List<Commit> parseGitLog(String filePath) throws GitLogException, IOException {
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException("Log file not found at: " + filePath);

        List<Commit> commits = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                // Validation logic for commit entry - must start with "commit"
                if (!line.startsWith("commit")) {
                    throw new FileFormatEntryException("Invalid Format: Expected 'commit' but found: " + line);
                }

                // Validate logic for Author entry - must start with "Author:"
                String authorLine = br.readLine();
                if (authorLine == null || !authorLine.startsWith("Author:")) {
                    throw new GitLogException("Incomplete info: Missing Author line");
                }

                // Validation logic for Date entry - must start with "Date:"
                String dateLine = br.readLine();
                if (dateLine == null || !dateLine.startsWith("Date:")) {
                    throw new GitLogException("Incomplete info: Missing Date line");
                }

                // Try to extract date - skip entries with invalid/missing dates
                LocalDate date;
                try {
                    date = extractDate(dateLine);
                } catch (GitLogException e) {
                    // Skip this entry if date is invalid or missing
                    br.readLine(); // Skip the message line
                    continue;
                }

                // Extract developer name from Author line
                String developer = extractDeveloperName(authorLine);

                // Read message (next line)
                String message = br.readLine();
                if (message == null) message = "";

                commits.add(new Commit(developer, date, message));
            }
        }
        return commits;
    }

    private String extractDeveloperName(String authorLine) {
        // Format: "Author: John Doe <john.doe@example.com>"
        // Extract: "John Doe"
        int startIdx = authorLine.indexOf(": ") + 2;
        int endIdx = authorLine.indexOf(" <");
        if (endIdx > startIdx) {
            return authorLine.substring(startIdx, endIdx);
        }
        return authorLine;
    }

    private LocalDate extractDate(String dateLine) throws GitLogException {
        // Format: "Date:   2026-02-20" or "Date:" (empty)
        // Extract the date part after "Date:"
        String dateStr = dateLine.substring(5).trim(); // Remove "Date:" and trim whitespace

        if (dateStr.isEmpty()) {
            throw new GitLogException("Date is missing");
        }

        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            throw new GitLogException("Invalid date format: " + dateStr);
        }
    }

    public Map<String, Long> getTotalCommitsPerDev(List<Commit> commits, LocalDate since) {
        return commits.stream().filter(c -> !c.date().isBefore(since)).collect(Collectors.groupingBy(Commit::developer, Collectors.counting()));
    }

    public List<String> getInactiveDevelopers(List<Commit> commits) {
        // Logic to find devs with 2-day gaps in sequence
        Map<String, List<Commit>> commitsByDev = commits.stream()
                .collect(Collectors.groupingBy(Commit::developer));

        List<String> inactiveDevelopers = new ArrayList<>();

        for (Map.Entry<String, List<Commit>> entry : commitsByDev.entrySet()) {
            List<LocalDate> dates = entry.getValue().stream()
                    .map(Commit::date)
                    .sorted()
                    .toList();

            // Check for 2-day gaps
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

    public List<String> getActiveDevelopers(List<Commit> commits) {
        // Logic to find devs WITHOUT 2-day gaps
        Map<String, List<Commit>> commitsByDev = commits.stream()
                .collect(Collectors.groupingBy(Commit::developer));

        List<String> activeDevelopers = new ArrayList<>();

        for (Map.Entry<String, List<Commit>> entry : commitsByDev.entrySet()) {
            List<LocalDate> dates = entry.getValue().stream()
                    .map(Commit::date)
                    .sorted()
                    .toList();

            // Check for 2-day gaps
            boolean hasGap = false;
            for (int i = 0; i < dates.size() - 1; i++) {
                long daysBetween = ChronoUnit.DAYS.between(dates.get(i), dates.get(i + 1));
                if (daysBetween >= 2) {
                    hasGap = true;
                    break;
                }
            }

            // Add to active list if no gaps found
            if (!hasGap) {
                activeDevelopers.add(entry.getKey());
            }
        }

        return activeDevelopers;
    }
}