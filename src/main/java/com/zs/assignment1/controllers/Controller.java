package com.zs.assignment1.controllers;

import com.zs.assignment1.services.Service;
import com.zs.assignment1.system.SystemInfo;

public class Controller {
    private final Service service = new Service();

    public void executeRequest() {
        System.out.println("--- Fetching System Information ---");
        SystemInfo data = service.fetchSystemData();
        displayResult(data);
    }

    private void displayResult(SystemInfo info) {
        // Using Getters instead of direct field access
        System.out.println("Current User: " + info.getUserName());
        System.out.println("Home Directory: " + info.getHomeDir());
        System.out.println("OS Version: " + info.getOsVersion());
        System.out.println("OS Build: " + info.getOsBuild());
        System.out.println("Cores: " + info.getCpuCores());
        System.out.println("SystemMemory: " + info.getSystemMemory() + " GB");
        System.out.println("SystemSpace: " + info.getSystemSpace() + " GB");
    }
}