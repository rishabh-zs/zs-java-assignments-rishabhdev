package com.zs.assignment5.models;

import java.time.LocalDate;

/**
 * The type Commit.
 */
public record Commit(String developer, LocalDate date, String message) {

}
