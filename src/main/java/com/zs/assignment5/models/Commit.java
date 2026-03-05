package com.zs.assignment5.models;

import java.time.LocalDate;

/**
 * The type Commit.
 */
public record Commit(String hash, String author, LocalDate date) {
}