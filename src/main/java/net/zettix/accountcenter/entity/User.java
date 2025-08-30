package net.zettix.accountcenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.zettix.accountcenter.entity.enums.Role;

import java.time.LocalDate;
@Getter @Setter
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private LocalDate dob;

}
