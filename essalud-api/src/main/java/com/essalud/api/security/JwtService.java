package com.essalud.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    // ==========================================
    // GENERAR TOKEN JWT
    // ==========================================

    public String generateToken(UserDetails userDetails) {

        Date fechaActual = new Date();

        Date fechaExpiracion = new Date(
                fechaActual.getTime() + expirationMs
        );

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(fechaActual)
                .expiration(fechaExpiracion)
                .signWith(signingKey)
                .compact();
    }

    // ==========================================
    // EXTRAER NOMBRE DE USUARIO
    // ==========================================

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    // ==========================================
    // EXTRAER FECHA DE EXPIRACION
    // ==========================================

    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    // ==========================================
    // EXTRAER INFORMACION DEL TOKEN
    // ==========================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    // ==========================================
    // COMPROBAR EXPIRACION
    // ==========================================

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // ==========================================
    // VALIDAR TOKEN Y USUARIO
    // ==========================================

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // ==========================================
    // DIAGNOSTICO DEL TOKEN JWT
    // ==========================================

    public void diagnosticarToken(String token) {

        try {

            String usuario = extractUsername(token);

            Date expiracion = extractExpiration(token);

            boolean expirado = expiracion.before(new Date());

            System.out.println("--------------------------------");
            System.out.println("DIAGNOSTICO JWT");
            System.out.println("--------------------------------");

            System.out.println("Firma: CORRECTA");
            System.out.println("Usuario: " + usuario);
            System.out.println("Fecha expiracion: " + expiracion);
            System.out.println("Token expirado: " + expirado);

            System.out.println("--------------------------------");

        } catch (Exception e) {

            System.err.println("--------------------------------");
            System.err.println("ERROR EN DIAGNOSTICO JWT");
            System.err.println("Tipo: " +
                    e.getClass().getSimpleName());

            System.err.println("Detalle: " +
                    e.getMessage());

            System.err.println("--------------------------------");
        }
    }
}
