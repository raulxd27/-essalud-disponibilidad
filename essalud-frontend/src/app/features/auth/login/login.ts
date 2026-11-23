import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  nombreUsuario = '';
  contrasena = '';

  cargando = false;
  mostrarContrasena = false;
  mensajeError = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  iniciarSesion(): void {

    this.mensajeError = '';

    if (!this.nombreUsuario.trim() || !this.contrasena) {
      this.mensajeError = 'Ingrese su usuario y contraseña.';
      return;
    }

    this.cargando = true;

    this.authService.login({
      nombreUsuario: this.nombreUsuario.trim(),
      contrasena: this.contrasena
    }).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/inicio']);
      },
      error: (error) => {
        this.cargando = false;

        if (error.status === 401 || error.status === 403) {
          this.mensajeError = 'Usuario o contraseña incorrectos.';
        } else if (error.status === 0) {
          this.mensajeError = 'No se pudo conectar con el servidor.';
        } else {
          this.mensajeError =
            error.error?.mensaje ||
            error.error?.message ||
            'Ocurrió un error al iniciar sesión.';
        }
      }
    });
  }
}
