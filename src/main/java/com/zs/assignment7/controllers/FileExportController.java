package com.zs.assignment7.controllers;

import com.zs.assignment7.services.FileExportService;

/**
 * The type File export controller.
 */
public class FileExportController {
    private final FileExportService fileExportService;

    /**
     * Instantiates a new File export controller.
     */
    public FileExportController() {
        this.fileExportService = new FileExportService();
    }

    /**
     * Execute file export flow.
     *
     * @param filePath the file path
     */
    public void executeFileExportFlow(String filePath) {
        fileExportService.exportDataToCompressedFile(filePath);
    }
}
