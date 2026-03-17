package edu.cit.cuadero.borrowbox.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String studentId;

    @Column(nullable = false)
    private String password;

    public User() {}

    public User(String fullName, String email, String password, String studentId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.studentId = studentId;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}