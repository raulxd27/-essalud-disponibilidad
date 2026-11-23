package com.essalud.api.dto;

public class LoginResponseDTO {

    private String token;
    private String tipoToken;
    private Integer usuarioId;
    private String nombreUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;

    public LoginResponseDTO(
            String token,
            Integer usuarioId,
            String nombreUsuario,
            String nombres,
            String apellidos,
            String correo,
            String rol) {

        this.token = token;
        this.tipoToken = "Bearer";
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }
}