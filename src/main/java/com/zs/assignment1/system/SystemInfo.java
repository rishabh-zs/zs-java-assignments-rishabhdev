package com.zs.assignment1.system;

public class SystemInfo {
    private final String userName;
    private final String homeDir;
    private final String osVersion;
    private final String osBuild;
    private final int cpuCores;
    private final long systemMemory;
    private final long systemSpace;

    // Constructor to initialize all fields at once
    public SystemInfo(String userName, String homeDir, String osVersion, String osBuild,
                      int cpuCores, long systemMemory, long systemSpace) {
        this.userName = userName;
        this.homeDir = homeDir;
        this.osVersion = osVersion;
        this.osBuild = osBuild;
        this.cpuCores = cpuCores;
        this.systemMemory = systemMemory;
        this.systemSpace = systemSpace;
    }

    // Getters
    public String getUserName() { return userName; }
    public String getHomeDir() { return homeDir; }
    public String getOsVersion() { return osVersion; }
    public String getOsBuild() { return osBuild; }
    public int getCpuCores() { return cpuCores; }
    public long getSystemMemory() { return systemMemory; }
    public long getSystemSpace() { return systemSpace; }
}