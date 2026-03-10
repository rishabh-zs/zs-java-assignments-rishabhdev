package com.zs.assignment7.services;

import com.zs.assignment7.repositories.FileExportRepository;

/**
 * The type File export service.
 */
public class FileExportService {
    private final FileExportRepository fileExportRepository;

    public FileExportService() {
        this.fileExportRepository = new FileExportRepository();

    }

    public void exportDataToCompressedFile(String filePath){
        try {
            fileExportRepository.exportDataToCompressedFile(filePath);
        } catch (Exception e) {
            System.err.println("Error exporting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
