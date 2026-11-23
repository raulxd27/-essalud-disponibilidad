import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Medicamento } from '../../core/models/medicamento.model';
import { MedicamentoService } from '../../core/services/medicamento';

@Component({
  selector: 'app-medicamentos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './medicamentos.html',
  styleUrl: './medicamentos.scss'
})
export class Medicamentos implements OnInit {

  private medicamentoService = inject(MedicamentoService);
  private cdr = inject(ChangeDetectorRef);

  medicamentos: Medicamento[] = [];
  medicamentosFiltrados: Medicamento[] = [];

  busqueda = '';
  cargando = false;
  mensajeError = '';

  ngOnInit(): void {
    this.listarMedicamentos();
  }

  listarMedicamentos(): void {

    this.cargando = true;
    this.mensajeError = '';

    this.medicamentoService.listar().subscribe({

      next: (datos) => {

        this.medicamentos = datos;
        this.filtrarMedicamentos();

        this.cargando = false;
        this.cdr.markForCheck();

        console.log('Medicamentos cargados:', this.medicamentos);
      },

      error: (error) => {

        console.error('Error al consultar medicamentos:', error);

        this.mensajeError =
          'No fue posible cargar los medicamentos. Verifique la conexión con el servidor.';

        this.cargando = false;
        this.cdr.markForCheck();
      }

    });
  }

  filtrarMedicamentos(): void {

    const termino = this.busqueda.trim().toLowerCase();

    this.medicamentosFiltrados =
      this.medicamentos.filter(medicamento =>
        [
          medicamento.nombre,
          medicamento.presentacion,
          medicamento.descripcion,
          medicamento.codigoPetitorio ?? ''
        ].some(valor =>
          valor.toLowerCase().includes(termino)
        )
      );

    this.cdr.markForCheck();
  }

  formatearPrecio(precio: number | null): string {

    if (precio === null || precio === undefined) {
      return 'No registrado';
    }

    return `S/ ${precio.toFixed(2)}`;
  }

}