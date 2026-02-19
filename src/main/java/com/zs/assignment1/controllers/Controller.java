//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zs.assignment1.controllers;

import com.zs.assignment1.services.Service;
import com.zs.assignment1.system.SystemInfo;

public class Controller {
    private Service service = new Service();

    public void executeRequest() {
        System.out.println("--- Fetching System Information ---");
        SystemInfo data = this.service.fetchSystemData();
        this.displayResult(data);
    }

    private void displayResult(SystemInfo info) {
        System.out.println("Current User: " + info.userName);
        System.out.println("Home Directory: " + info.homeDir);
        System.out.println("OS Version: " + info.osVersion);
        System.out.println("OS Build: " + info.osBuild);
        System.out.println("Cores: " + info.cpuCores);
        System.out.println("Memory: " + info.totalMemoryGB + " GB");
        System.out.println("Disk Size: " + info.totalDiskGB + " GB");
    }
}
