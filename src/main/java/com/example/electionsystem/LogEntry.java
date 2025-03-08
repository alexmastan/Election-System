package com.example.electionsystem;

public class LogEntry {
    private final int adminId;
    private final String loginTime;
    private final String ipAddress;

    public LogEntry(int adminId, String loginTime, String ipAddress) {
        this.adminId = adminId;
        this.loginTime = loginTime;
        this.ipAddress = ipAddress;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}