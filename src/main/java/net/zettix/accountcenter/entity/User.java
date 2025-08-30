package net.zettix.accountcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import net.zettix.accountcenter.entity.enums.Role;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate dob;

}
