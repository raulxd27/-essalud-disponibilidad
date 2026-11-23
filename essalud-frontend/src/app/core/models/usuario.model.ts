export interface LoginRequest {
  nombreUsuario: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  tipoToken: string;
  usuarioId: number;
  nombreUsuario: string;
  nombres: string;
  apellidos: string;
  correo: string;
  rol: string;
}

export interface Usuario {
  usuarioId: number;
  nombreUsuario: string;
  nombres: string;
  apellidos: string;
  correo: string;
  rol: string;
}
