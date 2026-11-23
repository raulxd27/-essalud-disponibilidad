import { Routes } from '@angular/router';

import { Login } from './features/auth/login/login';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [

  // LOGIN
  {
    path: 'login',
    component: Login
  },

  // PANEL PRINCIPAL
  {
    path: '',
    canActivate: [authGuard],

    loadComponent: () =>
      import('./features/dashboard/dashboard')
        .then(m => m.Dashboard),

    children: [

      // INICIO
      {
        path: 'inicio',
        loadComponent: () =>
          import('./features/dashboard/dashboard-home/dashboard-home')
            .then(m => m.DashboardHome)
      },

      // ESTABLECIMIENTOS
      {
        path: 'establecimientos',
        loadComponent: () =>
          import('./features/establecimientos/establecimientos')
            .then(m => m.Establecimientos)
      },

      // ESPECIALIDADES
      {
        path: 'especialidades',
        loadComponent: () =>
          import('./features/especialidades/especialidades')
            .then(m => m.Especialidades)
      },

      // MEDICAMENTOS
      {
        path: 'medicamentos',
        loadComponent: () =>
          import('./features/medicamentos/medicamentos')
            .then(m => m.Medicamentos)
      },

      // DISPONIBILIDAD DE CITAS
      {
        path: 'citas',
        loadComponent: () =>
          import('./features/citas/citas')
            .then(m => m.Citas)
      },

      // RUTA POR DEFECTO
      {
        path: '',
        redirectTo: 'inicio',
        pathMatch: 'full'
      }
    ]
  },

  // RUTAS NO ENCONTRADAS
  {
    path: '**',
    redirectTo: 'inicio'
  }

];