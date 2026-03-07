package edu.cit.cuadero.borrowbox.dto;

public class MeResponse {
    private Long id;
    private String fullName;
    private String email;

    public MeResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}