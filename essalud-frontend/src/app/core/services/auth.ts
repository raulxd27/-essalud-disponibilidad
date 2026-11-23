import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8080/api/auth';

  private readonly tokenKey = 'essalud_token';
  private readonly usuarioKey = 'essalud_usuario';

  readonly usuarioActual = signal<Usuario | null>(this.obtenerUsuario());
  readonly autenticado = signal<boolean>(!!this.obtenerToken());

  constructor(private http: HttpClient) {}

  login(datos: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      datos
    ).pipe(
      tap(respuesta => {
        localStorage.setItem(this.tokenKey, respuesta.token);

        const usuario: Usuario = {
          usuarioId: respuesta.usuarioId,
          nombreUsuario: respuesta.nombreUsuario,
          nombres: respuesta.nombres,
          apellidos: respuesta.apellidos,
          correo: respuesta.correo,
          rol: respuesta.rol
        };

        localStorage.setItem(
          this.usuarioKey,
          JSON.stringify(usuario)
        );

        this.usuarioActual.set(usuario);
        this.autenticado.set(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.usuarioKey);

    this.usuarioActual.set(null);
    this.autenticado.set(false);
  }

  obtenerToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  obtenerUsuario(): Usuario | null {
    const datos = localStorage.getItem(this.usuarioKey);

    if (!datos) {
      return null;
    }

    try {
      return JSON.parse(datos) as Usuario;
    } catch {
      localStorage.removeItem(this.usuarioKey);
      return null;
    }
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }

  tieneRol(roles: string[]): boolean {
    const usuario = this.obtenerUsuario();

    return !!usuario && roles.includes(usuario.rol);
  }
}
