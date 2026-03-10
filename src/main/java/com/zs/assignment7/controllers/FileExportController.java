package com.zs.assignment7.controllers;

import com.zs.assignment7.services.FileExportService;

/**
 * Database controller to manage the flow of data generation, database initialization, and file export .
 */
public class FileExportController {
    private final FileExportService fileExportService;

    public FileExportController() {
        this.fileExportService = new FileExportService();
    }

    /**
     * Execute flow for.
     */
    public void executeFileExportFlow(String filePath) {
        fileExportService.exportDataToCompressedFile(filePath);
    }
}
