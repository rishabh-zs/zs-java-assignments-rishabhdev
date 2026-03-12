package com.zs.assignment7.services;

import com.zs.assignment7.repositories.FileExportRepository;

/**
 * The type File export service.
 */
public class FileExportService {
    private final FileExportRepository fileExportRepository;

    /**
     * Instantiates a new File export service.
     */
    public FileExportService() {
        this.fileExportRepository = new FileExportRepository();

    }

    /**
     * Export data to compressed file.
     *
     * @param filePath the file path
     */
    public void exportDataToCompressedFile(String filePath){
        try {
            fileExportRepository.exportDataToCompressedFile(filePath);
        } catch (Exception e) {
            System.err.println("Error exporting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
