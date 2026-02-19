//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zs.assignment1.services;

import com.sun.management.OperatingSystemMXBean;
import com.zs.assignment1.system.SystemInfo;
import java.io.File;
import java.lang.management.ManagementFactory;

public class Service {
    public SystemInfo fetchSystemData() {
        SystemInfo info = new SystemInfo();
        OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        File root = new File("/");
        info.userName = System.getProperty("user.name");
        info.homeDir = System.getProperty("user.home");
        info.osVersion = System.getProperty("os.version");
        info.osBuild = System.getProperty("os.arch");
        info.cpuCores = Runtime.getRuntime().availableProcessors();
        info.totalMemoryGB = osBean.getTotalMemorySize() / 1073741824L;
        info.totalDiskGB = root.getTotalSpace() / 1073741824L;
        return info;
    }
}
