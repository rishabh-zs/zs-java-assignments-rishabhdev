package com.zs.assignment5.models;

import java.time.LocalDate;

public record Commit(String developer, LocalDate date, String message) {

}
