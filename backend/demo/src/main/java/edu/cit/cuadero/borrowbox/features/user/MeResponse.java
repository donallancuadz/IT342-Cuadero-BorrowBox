package edu.cit.cuadero.borrowbox.features.user;

public class MeResponse {
    private Long id;
    private String fullName;
    private String email;
    private String role;

    public MeResponse(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
