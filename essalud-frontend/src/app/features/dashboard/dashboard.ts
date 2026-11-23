import { Component, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  RouterModule,
  Router,
  NavigationEnd,
  Event
} from '@angular/router';

import { filter, Subscription } from 'rxjs';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnDestroy {

  private authService = inject(AuthService);
  private routerSubscription?: Subscription;

  router = inject(Router);

  usuario = this.authService.obtenerUsuario();

  tituloModulo = 'Panel principal';

  constructor() {

    this.actualizarTitulo(this.router.url);

    this.routerSubscription = this.router.events
      .pipe(
        filter((event: Event) => event instanceof NavigationEnd)
      )
      .subscribe((event) => {

        const navegacion = event as NavigationEnd;

        this.actualizarTitulo(navegacion.urlAfterRedirects);

      });
  }

  private actualizarTitulo(url: string): void {

    const ruta = url.split('?')[0].split('#')[0];

    const titulos: Record<string, string> = {

      '/inicio': 'Panel principal',

      '/establecimientos': 'Establecimientos de Salud',

      '/especialidades': 'Especialidades Médicas',

      '/medicamentos': 'Medicamentos',

      '/citas': 'Disponibilidad de Citas',

      '/auditoria': 'Auditoría del Sistema'

    };

    this.tituloModulo = titulos[ruta] ?? 'Panel principal';
  }

  cerrarSesion(): void {

    this.authService.logout();

    this.router.navigate(['/login']);

  }

  ngOnDestroy(): void {

    this.routerSubscription?.unsubscribe();

  }
}