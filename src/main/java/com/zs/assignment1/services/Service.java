package com.zs.assignment1.services;

import com.sun.management.OperatingSystemMXBean;
import com.zs.assignment1.system.SystemInfo;
import java.io.File;
import java.lang.management.ManagementFactory;

public class Service {
    public SystemInfo fetchSystemData() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        File root = new File("/");

        // Gather data into local variables
        String user = System.getProperty("user.name");
        String home = System.getProperty("user.home");
        String version = System.getProperty("os.version");
        String arch = System.getProperty("os.arch");
        int cores = Runtime.getRuntime().availableProcessors();
        long memory = osBean.getTotalMemorySize() / 1073741824L;
        long space = root.getTotalSpace() / 1073741824L;

        // Return a new initialized object
        return new SystemInfo(user, home, version, arch, cores, memory, space);
    }
}