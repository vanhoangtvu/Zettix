package net.zettix.accountcenter.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.zettix.accountcenter.entity.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotEmpty(message = "enter username")
    private String username;
    private String password;
}
