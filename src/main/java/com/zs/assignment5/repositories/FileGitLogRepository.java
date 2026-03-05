package com.zs.assignment5.repositories;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * The type File git log repository.
 */
public class FileGitLogRepository {

    private static final DateTimeFormatter GIT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);

    /**
     * Fetch all commits list.
     *
     * @param filePath the file path
     * @return the list
     * @throws FileNotFoundException               the file not found exception
     * @throws FileFormatException                 the file format exception
     * @throws FileInformationIncompeleteException the file information incompelete exception
     */
    public List<Commit> fetchAllCommits(String filePath) throws FileNotFoundException, FileFormatException, FileInformationIncompeleteException {
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
}