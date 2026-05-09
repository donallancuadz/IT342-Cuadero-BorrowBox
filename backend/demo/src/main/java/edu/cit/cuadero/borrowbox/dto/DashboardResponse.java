package edu.cit.cuadero.borrowbox.dto;

import java.util.List;

public class DashboardResponse {

    private long activeBorrows;
    private long pendingRequests;
    private long returnedItems;
    private long availableItems;
    private List<RecentRequestItem> recentRequests;

    public DashboardResponse(long activeBorrows, long pendingRequests,
                              long returnedItems, long availableItems,
                              List<RecentRequestItem> recentRequests) {
        this.activeBorrows = activeBorrows;
        this.pendingRequests = pendingRequests;
        this.returnedItems = returnedItems;
        this.availableItems = availableItems;
        this.recentRequests = recentRequests;
    }

    public static class RecentRequestItem {
        private String itemName;
        private String requestDate;
        private String status;

        public RecentRequestItem(String itemName, String requestDate, String status) {
            this.itemName = itemName;
            this.requestDate = requestDate;
            this.status = status;
        }

        public String getItemName() { return itemName; }
        public String getRequestDate() { return requestDate; }
        public String getStatus() { return status; }
    }

    public long getActiveBorrows() { return activeBorrows; }
    public long getPendingRequests() { return pendingRequests; }
    public long getReturnedItems() { return returnedItems; }
    public long getAvailableItems() { return availableItems; }
    public List<RecentRequestItem> getRecentRequests() { return recentRequests; }
}