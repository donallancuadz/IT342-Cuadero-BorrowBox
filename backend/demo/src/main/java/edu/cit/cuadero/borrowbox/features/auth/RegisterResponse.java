package edu.cit.cuadero.borrowbox.features.auth;

public class RegisterResponse {
    private Long id;
    private String fullName;
    private String email;

    public RegisterResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}