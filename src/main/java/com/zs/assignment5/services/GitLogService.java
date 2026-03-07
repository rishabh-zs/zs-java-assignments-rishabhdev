package com.zs.assignment5.services;

import com.zs.assignment5.exceptions.FileFormatException;
import com.zs.assignment5.exceptions.FileInformationIncompleteException;
import com.zs.assignment5.exceptions.FileNotFoundException;
import com.zs.assignment5.models.Commit;
import com.zs.assignment5.repositories.FileGitLogRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Git log service.
 */
public class GitLogService {

    private final FileGitLogRepository repository;

    /**
     * Instantiates a new Git log service.
     *
     * @param repository the repository
     */
    public GitLogService(FileGitLogRepository repository) {
        this.repository = repository;
    }

    /**
     * Gets commits.
     *
     * @param source the source
     * @return the commits
     * @throws FileNotFoundException               the file not found exception
     * @throws FileFormatException                 the file format exception
     * @throws FileInformationIncompleteException the file information incompelete exception
     */
    public List<Commit> getCommits(String source) throws FileNotFoundException, FileFormatException, FileInformationIncompleteException {
        return repository.fetchAllCommits(source);
    }

    /**
     * Gets total commits since.
     *
     * @param commits   the commits
     * @param sinceDate the since date
     * @return the total commits since
     */
    public Map<String, Long> getTotalCommitsSince(List<Commit> commits, LocalDate sinceDate) {
        return commits
                .stream()
                .filter(c -> !c.date().isBefore(sinceDate))
                .collect(Collectors.groupingBy(Commit::author, Collectors.counting()));
    }

    /**
     * Gets daily commits since.
     *
     * @param commits   the commits
     * @param sinceDate the since date
     * @return the daily commits since
     */
    public Map<String, Map<LocalDate, Long>> getDailyCommitsSince(List<Commit> commits, LocalDate sinceDate) {
        return commits
                .stream()
                .filter(c -> !c.date().isBefore(sinceDate))
                .collect(Collectors.groupingBy(
                        Commit::author,
                        Collectors.groupingBy(Commit::date, Collectors.counting())
                ));
    }

    /**
     * Gets developers with inactivity.
     *
     * @param commits the commits
     * @return the developers with inactivity
     */
    public List<String> getDevelopersWithInactivity(List<Commit> commits) {
        Map<String, List<LocalDate>> developerDates = commits
                .stream()
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
     * Gets active developers.
     *
     * @param commits   the commits
     * @param sinceDate the since date
     * @return the active developers
     */
    public Set<String> getActiveDevelopers(List<Commit> commits, LocalDate sinceDate) {
        LocalDate endDate = sinceDate.plusDays(2);

        return commits
                .stream()
                .filter(c -> !c.date().isBefore(sinceDate) && !c.date().isAfter(endDate))
                .map(Commit::author)
                .collect(Collectors.toSet());
    }

    /**
     * Gets inactive developers.
     *
     * @param commits   the commits
     * @param sinceDate the since date
     * @return the inactive developers
     */
    public Set<String> getInactiveDevelopers(List<Commit> commits, LocalDate sinceDate) {
        Set<String> allDevelopers = commits
                .stream()
                .map(Commit::author)
                .collect(Collectors.toSet());

        Set<String> activeDevelopers = getActiveDevelopers(commits, sinceDate);

        allDevelopers.removeAll(activeDevelopers);
        return allDevelopers;
    }
}