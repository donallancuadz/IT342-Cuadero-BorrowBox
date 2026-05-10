package edu.cit.cuadero.borrowbox.features.requests;

import java.time.LocalDateTime;

public class AdminBorrowRequestResponse {
    private Long id;
    private Long itemId;
    private String itemName;
    private Long userId;
    private String borrowerName;
    private String borrowerEmail;
    private String status;
    private LocalDateTime requestDate;

    public AdminBorrowRequestResponse(
            Long id,
            Long itemId,
            String itemName,
            Long userId,
            String borrowerName,
            String borrowerEmail,
            String status,
            LocalDateTime requestDate
    ) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
        this.borrowerName = borrowerName;
        this.borrowerEmail = borrowerEmail;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Long getId() { return id; }
    public Long getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public Long getUserId() { return userId; }
    public String getBorrowerName() { return borrowerName; }
    public String getBorrowerEmail() { return borrowerEmail; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestDate() { return requestDate; }
}
