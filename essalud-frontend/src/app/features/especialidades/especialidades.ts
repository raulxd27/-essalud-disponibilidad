import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Especialidad } from '../../core/models/especialidad.model';
import { EspecialidadService } from '../../core/services/especialidad';

@Component({
  selector: 'app-especialidades',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './especialidades.html',
  styleUrl: './especialidades.scss'
})
export class Especialidades implements OnInit {

  private especialidadService = inject(EspecialidadService);
  private cdr = inject(ChangeDetectorRef);

  especialidades: Especialidad[] = [];
  especialidadesFiltradas: Especialidad[] = [];

  busqueda = '';
  cargando = false;
  mensajeError = '';

  ngOnInit(): void {
    this.listarEspecialidades();
  }

  listarEspecialidades(): void {

    this.cargando = true;
    this.mensajeError = '';

    this.especialidadService.listar().subscribe({

      next: (datos) => {

        this.especialidades = datos;
        this.filtrarEspecialidades();

        this.cargando = false;
        this.cdr.markForCheck();

        console.log('Especialidades cargadas:', this.especialidades);
      },

      error: (error) => {

        console.error('Error al consultar especialidades:', error);

        this.mensajeError =
          'No fue posible cargar las especialidades. Verifique la conexión con el servidor.';

        this.cargando = false;
        this.cdr.markForCheck();
      }

    });
  }

  filtrarEspecialidades(): void {

    const termino = this.busqueda.trim().toLowerCase();

    this.especialidadesFiltradas =
      this.especialidades.filter(especialidad =>
        [
          especialidad.nombre,
          especialidad.descripcion
        ].some(valor =>
          (valor ?? '').toLowerCase().includes(termino)
        )
      );

    this.cdr.markForCheck();
  }
}