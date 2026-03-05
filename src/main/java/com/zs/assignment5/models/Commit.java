package com.zs.assignment5.models;

import java.time.LocalDate;

/**
 * Represents a single parsed Git commit.
 */
public record Commit(String hash, String author, LocalDate date) {
}