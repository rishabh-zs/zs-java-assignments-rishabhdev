# Assignment 5: Git Log Parser - Complete Documentation

## Overview

Assignment 5 is a comprehensive Git log parsing application that analyzes developer activity patterns. The application reads a Git log file, extracts commit information, and provides statistics about developer commits and activity continuity.

## Objective

Parse a Git log file to:
1. Count total commits per developer since a specific date
2. Identify active developers (those without 2-day gaps in commits)
3. Identify inactive developers (those with 2-day gaps or more in commits)

## Features

### Core Functionality

1. **Git Log Parsing** - Reads and parses Git log entries with validation
2. **Date Filtering** - Filters commits based on a reference date
3. **Developer Analytics** - Groups and analyzes commits by developer
4. **Gap Detection** - Detects inactivity patterns (2-day gaps or more)
5. **Error Handling** - Comprehensive exception handling for various error scenarios

### Output

The program displays:
- Total commits per developer (since specified date)
- Reference date used for active developer analysis
- List of active developers (without 2-day gaps)
- List of inactive developers (with 2-day gaps)

## Architecture

### Three-Tier Architecture

```
Presentation Layer
└── GitLogController
    │
Service Layer
└── GitLogService
    │
Data Layer
└── Commit (Record)
```

### Class Descriptions

#### 1. **Main.java** (Entry Point)
- Accepts command-line arguments: file path and date
- Default values: `src/packages/sample_git.log` and `2026-02-20`
- Delegates processing to GitLogController

#### 2. **GitLogController.java** (Controller/Orchestrator)
- Orchestrates the processing flow
- Parses the input date
- Calls service methods and displays results
- Handles exceptions gracefully

Key outputs:
- Total commits per developer
- Active developers (with date reference)
- Inactive developers

#### 3. **GitLogService.java** (Business Logic)
- Parses Git log file with validation
- Extracts developer name and date from commit entries
- Implements developer activity analysis
- Provides methods for getting active and inactive developers

Key methods:
- `parseGitLog(String filePath)` - Reads and parses the log file
- `getTotalCommitsPerDev(List<Commit>, LocalDate)` - Counts commits per dev since date
- `getActiveDevelopers(List<Commit>)` - Returns developers without gaps
- `getInactiveDevelopers(List<Commit>)` - Returns developers with gaps

#### 4. **Commit.java** (Data Model)
- Immutable record class for storing commit information
- Automatically provides equals(), hashCode(), toString()

#### 5. **ParserInfo.java** (Annotation)
- Custom annotation for marking the parser service
- Available at runtime for reflection-based operations
- Version tracking

#### 6. **Exception Classes**
- `GitLogException.java` - General git log processing errors
- `FileFormatEntryException.java` - Format validation errors

## Implementation Details

### Parsing Logic

The parser reads Git log entries in the following format:
```
commit <hash>
Author: <name> <email>
Date:   <date>
<message>
```

**Validation Steps:**
1. Each line must start with "commit"
2. Next line must be "Author:"
3. Following line must be "Date:"
4. Extract developer name, date, and message
5. Skip entries with invalid or missing dates

### Developer Activity Analysis

**Active Developers:**
- Developers with NO gaps of 2 days or more between consecutive commits
- Indicates continuous engagement with the project

**Inactive Developers:**
- Developers with one or more gaps of 2 days or more between consecutive commits
- Indicates periods of inactivity

**Gap Calculation:**
```
long daysBetween = ChronoUnit.DAYS.between(date1, date2);
if (daysBetween >= 2) {
    // Developer has inactivity gap
}
```

## Key Java Concepts

### 1. Streams API
- Functional programming paradigm
- Filter and group operations
- Collectors for aggregation

### 2. Date/Time API
- Modern date handling (replaces legacy Calendar)
- Precise time calculations
- Temporal units (DAYS, HOURS, etc.)

### 3. Record Classes
- Immutable data carriers (Java 14+)
- Auto-generated equals(), hashCode(), toString()
- Compact syntax

### 4. Custom Exceptions
- Domain-specific error handling
- Graceful error recovery
- Informative error messages

### 5. Lambda Expressions & Method References
- Concise functional syntax
- Improved code readability

### 6. Collections API
- List, Map operations
- Grouping and sorting
- Stream operations

## Git Log File Format

The program expects the following format:

```
commit abc123def456
Author: John Doe <john.doe@example.com>
Date:   2026-02-20
First commit message

commit xyz789uvw012
Author: Jane Smith <jane.smith@example.com>
Date:   2026-02-22
Another commit message
```

**Requirements:**
- Each entry starts with "commit" (any hash value)
- Followed by "Author:" line with name and email
- Followed by "Date:" line with date in YYYY-MM-DD format
- Followed by commit message (can be empty)
- Entries separated by blank lines

## Error Handling

The application handles:

1. **File Not Found** - Reports missing file with path
2. **Invalid Format** - Detects missing "commit" keyword
3. **Missing Author/Date** - Validates presence of required fields
4. **Invalid Date Format** - Catches and reports date parsing errors
5. **Empty Dates** - Skips entries with missing dates
6. **IOException** - Handles file reading errors

All errors are caught and displayed with descriptive messages to help debugging.

## Testing Scenarios

The implementation correctly handles:

✅ Multiple developers with different commit patterns
✅ Developers with no gaps (continuous activity)
✅ Developers with multiple gaps (identifies first gap)
✅ Single-commit developers (no gaps to check)
✅ Empty commit lists (returns empty lists)
✅ Invalid date formats (skips entries with errors)
✅ Missing required fields (throws exceptions)
✅ Large files (efficient streaming with BufferedReader)
✅ Date filtering (only counts commits since specified date)

## Build and Execution

### Build Command
```bash
./gradlew clean build
```

### Run Assignment 5
```bash
./gradlew runAssignment5
```

Or manually:
```bash
./gradlew runAssignment5 --args="path/to/log/file.log 2026-02-20"
```

### Expected Output
```
Total Commits: {developer1=5, developer2=3, ...}
Considering Active Developers since: 2026-02-20
Active Developers (without 2-day gaps): [dev1, dev3, ...]
InActive Developers (with 2-day gaps): [dev2, dev4, ...]
-----Finished processing git log--------
```

## Project Structure

```
src/main/java/com/zs/assignment5/
├── Main.java                    # Entry point
├── controllers/
│   └── GitLogController.java   # Flow orchestration
├── services/
│   └── GitLogService.java      # Business logic
├── models/
│   └── Commit.java             # Data model (Record)
├── annotations/
│   └── ParserInfo.java         # Custom annotation
└── exceptions/
    ├── GitLogException.java
    └── FileFormatEntryException.java
```

## Build Gradle Configuration

```groovy
tasks.register('runAssignment5', JavaExec) {
    group = 'execution'
    description = 'Run Assignment 5 - Git Log Parser'
    classpath = sourceSets.main.runtimeClasspath
    mainClass = 'com.zs.assignment5.Main'
    args = ['src/packages/sample_git.log', '2026-02-20']
}
```

## Summary

Assignment 5 demonstrates a complete, production-ready Java application featuring:

- ✅ Clean three-tier architecture
- ✅ Modern Java features (Records, Streams, Date/Time API, Lambda expressions)
- ✅ Robust error handling with custom exceptions
- ✅ Stream processing for data analysis and aggregation
- ✅ Proper separation of concerns
- ✅ Flexible command-line arguments with sensible defaults
- ✅ Comprehensive output with meaningful statistics
- ✅ Developer activity pattern detection
- ✅ Efficient file processing with BufferedReader
- ✅ Custom annotations for metadata

The application successfully parses Git logs, analyzes developer activity patterns, and provides actionable insights about commitment continuity and inactivity periods.

---

**Assignment Type**: Git Log Analysis & Stream Processing
**Concepts**: Streams API, Records, Date/Time API, Custom Exceptions, Collections, Lambda Expressions
**Last Updated**: February 25, 2026
**Status**: Complete and Tested ✅

