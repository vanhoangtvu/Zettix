package net.zettix.accountcenter.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import net.zettix.accountcenter.dto.request.AuthenticationRequest;
import net.zettix.accountcenter.dto.request.IntrospectRequest;
import net.zettix.accountcenter.dto.response.AuthenticationResponse;
import net.zettix.accountcenter.dto.response.IntrospectResponse;
import net.zettix.accountcenter.entity.User;
import net.zettix.accountcenter.entity.enums.Role;
import net.zettix.accountcenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {

        String token = request.getToken();

        // 1) Parse & verify signature
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); // HS512: key >= 64 bytes
        boolean signatureOk = signedJWT.verify(verifier);

        // 2) Check expiration (nếu không có exp thì coi như không hợp lệ)
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean notExpired = (expiryTime != null) && expiryTime.after(new Date());

        // 3) Build response
        return IntrospectResponse.builder()
                .valid(signatureOk && notExpired)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new RuntimeException("Incorrect username or password"));
        PasswordEncoder passwordEncoder =new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticate){
            throw new RuntimeException("Unauthenticate");
        }
        var token =generateToken(user);{
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        }
    }
    private String generateToken(User user){
        JWSHeader header =new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet =new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("zettix.net")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("Role",buildScope(user))
                .build();

        Payload payload =new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject =new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();

        }catch (JOSEException e){
            log.error("can not create token",e);
            throw  new RuntimeException(e);
        }
    }
    private String buildScope(User user) {
        return user.getRole() != null ? user.getRole().name() : "";
    }
}
