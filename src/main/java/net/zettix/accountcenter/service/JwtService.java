package net.zettix.accountcenter.service;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.signerKey}")
    private String signerKey; // HS512: key >= 64 bytes

    public String extractUsername(String token) {
        try {
            var signed = SignedJWT.parse(token);
            if (!signed.verify(new MACVerifier(signerKey.getBytes()))) return null;
            return signed.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        try {
            var signed = SignedJWT.parse(token);
            if (!signed.verify(new MACVerifier(signerKey.getBytes()))) return List.of();

            var claims = signed.getJWTClaimsSet();
            // bạn đang set 1 role duy nhất → lấy từ "role"
            String role = claims.getStringClaim("role"); // ví dụ "ADMIN" / "USER"
            if (role == null) return List.of();
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        } catch (Exception e) {
            return List.of();
        }
    }
}

