package edu.cit.cuadero.borrowbox.dto;

public class UserAdminResponse {
    private Long id;
    private String fullName;
    private String email;
    private String studentId;
    private String role;

    public UserAdminResponse(Long id, String fullName, String email, String studentId, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.studentId = studentId;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getStudentId() { return studentId; }
    public String getRole() { return role; }
}
