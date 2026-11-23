import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Establecimiento } from '../../core/models/establecimiento.model';
import { EstablecimientoService } from '../../core/services/establecimiento';

@Component({
  selector: 'app-establecimientos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './establecimientos.html',
  styleUrl: './establecimientos.scss'
})
export class Establecimientos implements OnInit {

  private establecimientoService = inject(EstablecimientoService);
  private cdr = inject(ChangeDetectorRef);

  establecimientos: Establecimiento[] = [];
  establecimientosFiltrados: Establecimiento[] = [];

  busqueda = '';
  cargando = false;
  mensajeError = '';

  ngOnInit(): void {
    this.listarEstablecimientos();
  }

  listarEstablecimientos(): void {

    this.cargando = true;
    this.mensajeError = '';

    this.establecimientoService.listar().subscribe({

      next: (datos) => {

        this.establecimientos = datos;

        this.filtrarEstablecimientos();

        this.cargando = false;

        this.cdr.markForCheck();

        console.log(
          'Establecimientos cargados:',
          this.establecimientos
        );
      },

      error: (error) => {

        console.error(
          'Error al consultar establecimientos:',
          error
        );

        this.mensajeError =
          'No fue posible cargar los establecimientos. Verifique la conexión con el servidor.';

        this.cargando = false;

        this.cdr.markForCheck();
      }

    });
  }

  filtrarEstablecimientos(): void {

    const termino = this.busqueda.trim().toLowerCase();

    this.establecimientosFiltrados =
      this.establecimientos.filter(e =>
        [
          e.nombre,
          e.direccion,
          e.distrito,
          e.provincia,
          e.departamento
        ].some(valor =>
          (valor ?? '').toLowerCase().includes(termino)
        )
      );

    this.cdr.markForCheck();
  }
}