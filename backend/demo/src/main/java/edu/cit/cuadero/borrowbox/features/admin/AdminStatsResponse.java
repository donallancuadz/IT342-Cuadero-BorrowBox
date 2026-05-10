package edu.cit.cuadero.borrowbox.features.admin;

public class AdminStatsResponse {
    private long totalUsers;
    private long admins;
    private long totalItems;
    private long availableItems;
    private long totalRequests;
    private long pendingRequests;
    private long approvedRequests;
    private long rejectedRequests;

    public AdminStatsResponse(
            long totalUsers,
            long admins,
            long totalItems,
            long availableItems,
            long totalRequests,
            long pendingRequests,
            long approvedRequests,
            long rejectedRequests
    ) {
        this.totalUsers = totalUsers;
        this.admins = admins;
        this.totalItems = totalItems;
        this.availableItems = availableItems;
        this.totalRequests = totalRequests;
        this.pendingRequests = pendingRequests;
        this.approvedRequests = approvedRequests;
        this.rejectedRequests = rejectedRequests;
    }

    public long getTotalUsers() { return totalUsers; }
    public long getAdmins() { return admins; }
    public long getTotalItems() { return totalItems; }
    public long getAvailableItems() { return availableItems; }
    public long getTotalRequests() { return totalRequests; }
    public long getPendingRequests() { return pendingRequests; }
    public long getApprovedRequests() { return approvedRequests; }
    public long getRejectedRequests() { return rejectedRequests; }
}
