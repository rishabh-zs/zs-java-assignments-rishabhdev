package com.zs.assignment5.services;

import com.zs.assignment5.exceptions.FileFormatException;
import com.zs.assignment5.exceptions.FileInformationIncompeleteException;
import com.zs.assignment5.exceptions.FileNotFoundException;
import com.zs.assignment5.models.Commit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class GitLogService {

    private static final DateTimeFormatter GIT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);

    public List<Commit> parseLog(String filePath) throws FileNotFoundException, FileFormatException, FileInformationIncompeleteException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("The file at path '" + filePath + "' was not found.");
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new FileNotFoundException("Could not read the file: " + e.getMessage(), e);
        }

        List<Commit> commits = new ArrayList<>();
        String currentHash = null;
        String currentAuthor = null;
        LocalDate currentDate = null;
        boolean inCommitBlock = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("commit ")) {
                if (inCommitBlock) {
                    validateAndAddCommit(commits, currentHash, currentAuthor, currentDate);
                }
                currentHash = line.substring(7).trim();
                currentAuthor = null;
                currentDate = null;
                inCommitBlock = true;
            }
            else if (line.startsWith("Author:")) {
                if (!inCommitBlock) throw new FileFormatException("Found Author line without a preceding commit hash at line " + (i + 1));
                currentAuthor = line.substring(7).replaceAll("<.*>", "").trim();
            }
            else if (line.startsWith("Date:")) {
                if (!inCommitBlock) throw new FileFormatException("Found Date line without a preceding commit hash at line " + (i + 1));

                String dateStr = line.substring(5).trim();
                try {
                    currentDate = ZonedDateTime.parse(dateStr, GIT_DATE_FORMATTER).toLocalDate();
                } catch (Exception e) {
                    throw new FileFormatException("Incorrect date format at line " + (i + 1) + ": " + dateStr);
                }
            }
        }

        if (inCommitBlock) {
            validateAndAddCommit(commits, currentHash, currentAuthor, currentDate);
        }

        return commits;
    }

    private void validateAndAddCommit(List<Commit> commits, String hash, String author, LocalDate date) throws FileInformationIncompeleteException {
        if (author == null || date == null) {
            throw new FileInformationIncompeleteException("Commit " + hash + " has incomplete information.");
        }
        commits.add(new Commit(hash, author, date));
    }

    public Map<String, Long> getTotalCommitsSince(List<Commit> commits, LocalDate sinceDate) {
        return commits.stream()
                .filter(c -> !c.date().isBefore(sinceDate))
                .collect(Collectors.groupingBy(Commit::author, Collectors.counting()));
    }

    public Map<String, Map<LocalDate, Long>> getDailyCommitsSince(List<Commit> commits, LocalDate sinceDate) {
        return commits.stream()
                .filter(c -> !c.date().isBefore(sinceDate))
                .collect(Collectors.groupingBy(
                        Commit::author,
                        Collectors.groupingBy(Commit::date, Collectors.counting())
                ));
    }

    public List<String> getDevelopersWithInactivity(List<Commit> commits) {
        Map<String, List<LocalDate>> developerDates = commits.stream()
                .collect(Collectors.groupingBy(
                        Commit::author,
                        Collectors.mapping(Commit::date, Collectors.toList())
                ));

        List<String> inactiveDevs = new ArrayList<>();

        for (Map.Entry<String, List<LocalDate>> entry : developerDates.entrySet()) {
            List<LocalDate> dates = entry.getValue();
            Collections.sort(dates);

            boolean hasGap = false;
            for (int i = 0; i < dates.size() - 1; i++) {
                if (ChronoUnit.DAYS.between(dates.get(i), dates.get(i + 1)) > 2) {
                    hasGap = true;
                    break;
                }
            }
            if (hasGap) {
                inactiveDevs.add(entry.getKey());
            }
        }
        return inactiveDevs;
    }

    /**
     * Active Developers: Made at least 1 commit within 2 days from the provided date 'd'.
     */
    public Set<String> getActiveDevelopers(List<Commit> commits, LocalDate sinceDate) {
        LocalDate endDate = sinceDate.plusDays(2);

        return commits.stream()
                .filter(c -> !c.date().isBefore(sinceDate) && !c.date().isAfter(endDate))
                .map(Commit::author)
                .collect(Collectors.toSet());
    }

    /**
     * Inactive Developers: Developers existing in the log who made 0 commits within 2 days from 'd'.
     */
    public Set<String> getInactiveDevelopers(List<Commit> commits, LocalDate sinceDate) {
        Set<String> allDevelopers = commits.stream()
                .map(Commit::author)
                .collect(Collectors.toSet());

        Set<String> activeDevelopers = getActiveDevelopers(commits, sinceDate);

        // Those who are not active in the 2-day window are considered inactive for that window
        allDevelopers.removeAll(activeDevelopers);
        return allDevelopers;
    }
}