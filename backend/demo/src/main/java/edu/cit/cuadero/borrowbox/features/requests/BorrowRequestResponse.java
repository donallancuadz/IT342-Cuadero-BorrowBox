package edu.cit.cuadero.borrowbox.features.requests;

import java.time.LocalDateTime;

public class BorrowRequestResponse {

    private Long id;
    private Long itemId;
    private String itemName;
    private String status;
    private LocalDateTime requestDate;

    public BorrowRequestResponse(Long id, Long itemId, String itemName, String status, LocalDateTime requestDate) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Long getId() { return id; }
    public Long getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestDate() { return requestDate; }
}