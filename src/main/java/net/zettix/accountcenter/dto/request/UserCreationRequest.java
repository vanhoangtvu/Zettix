package net.zettix.accountcenter.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @Size(min = 3,message = "username must be at least 3 characters")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be at least 8 characters and contain 1 uppercase, 1 lowercase, 1 digit and 1 special character")
    private String password;

    private String fullname;
    @Email @NotEmpty

    private String email;

    @Past
    private LocalDate dob;

}
