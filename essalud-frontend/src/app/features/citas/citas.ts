import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  Cita,
  CrearCita
} from '../../core/models/cita.model';

import { Especialidad } from '../../core/models/especialidad.model';
import { Establecimiento } from '../../core/models/establecimiento.model';

import { CitaService } from '../../core/services/cita';
import { EspecialidadService } from '../../core/services/especialidad';
import { EstablecimientoService } from '../../core/services/establecimiento';

@Component({
  selector: 'app-citas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './citas.html',
  styleUrl: './citas.scss'
})
export class Citas implements OnInit {

  private citaService = inject(CitaService);
  private especialidadService = inject(EspecialidadService);
  private establecimientoService = inject(EstablecimientoService);
  private cdr = inject(ChangeDetectorRef);

  // ==========================================
  // DATOS DEL MÓDULO
  // ==========================================

  citas: Cita[] = [];
  especialidades: Especialidad[] = [];
  establecimientos: Establecimiento[] = [];

  // ==========================================
  // FILTROS
  // ==========================================

  especialidadId: number | null = null;
  establecimientoId: number | null = null;
  fecha = '';

  // ==========================================
  // FECHA MÍNIMA: MAÑANA
  // ==========================================

  fechaMinima = this.obtenerFechaManana();

  // ==========================================
  // ESTADOS DE CONSULTA
  // ==========================================

  cargando = false;
  mensajeError = '';
  mensajeExito = '';

  // ==========================================
  // ESTADOS DEL FORMULARIO
  // ==========================================

  mostrarFormulario = false;
  guardando = false;
  errorFormulario = '';

  // CONTROL DE EDICIÓN
  editandoId: number | null = null;

  // CONTROL DE CAMBIO DE ESTADO
  cambiandoEstadoId: number | null = null;

  nuevaCita: CrearCita = this.crearFormularioVacio();

  // ==========================================
  // INICIALIZACIÓN
  // ==========================================

  ngOnInit(): void {
    this.cargarEspecialidades();
    this.cargarEstablecimientos();
  }

  // ==========================================
  // FORMULARIO VACÍO
  // ==========================================

  private crearFormularioVacio(): CrearCita {
    return {
      especialidadId: 0,
      establecimientoId: 0,
      fechaAtencion: '',
      horaInicio: '',
      horaFin: '',
      cantidadCupos: 0
    };
  }

  // ==========================================
  // CARGAR ESPECIALIDADES
  // ==========================================

  cargarEspecialidades(): void {

    this.especialidadService.listar().subscribe({

      next: datos => {
        this.especialidades = datos;
        this.cdr.markForCheck();
      },

      error: error => {
        console.error('Error al cargar especialidades:', error);

        this.mensajeError =
          'No fue posible cargar las especialidades.';

        this.cdr.markForCheck();
      }

    });

  }

  // ==========================================
  // CARGAR ESTABLECIMIENTOS ACTIVOS
  // ==========================================

  cargarEstablecimientos(): void {

    this.establecimientoService.listar().subscribe({

      next: datos => {

        this.establecimientos = datos.filter(
          establecimiento => establecimiento.activo
        );

        this.cdr.markForCheck();
      },

      error: error => {

        console.error('Error al cargar establecimientos:', error);

        this.mensajeError =
          'No fue posible cargar los establecimientos.';

        this.cdr.markForCheck();
      }

    });

  }

  // ==========================================
  // CONSULTAR DISPONIBILIDAD
  // ==========================================

  consultarCitas(limpiarMensajes: boolean = true): void {

    if (limpiarMensajes) {
      this.mensajeError = '';
      this.mensajeExito = '';
    }

    if (!this.especialidadId && !this.establecimientoId) {

      this.mensajeError =
        'Seleccione una especialidad o un establecimiento para consultar.';

      this.cdr.markForCheck();
      return;
    }

    if (this.establecimientoId && !this.fecha) {

      this.mensajeError =
        'Seleccione una fecha cuando consulte por establecimiento.';

      this.cdr.markForCheck();
      return;
    }

    this.cargando = true;
    this.citas = [];

    this.citaService.listar(
      this.especialidadId ?? undefined,
      this.establecimientoId ?? undefined,
      this.fecha || undefined
    ).subscribe({

      next: datos => {

        this.citas = datos;
        this.cargando = false;

        this.cdr.markForCheck();
      },

      error: error => {

        console.error('Error al consultar citas:', error);

        this.mensajeError =
          error.error?.message ??
          'No fue posible consultar la disponibilidad de citas.';

        this.cargando = false;

        this.cdr.markForCheck();
      }

    });

  }

  // ==========================================
  // ABRIR FORMULARIO PARA NUEVO REGISTRO
  // ==========================================

  abrirFormulario(): void {

    if (this.guardando) {
      return;
    }

    this.editandoId = null;

    this.nuevaCita = this.crearFormularioVacio();

    this.errorFormulario = '';
    this.mensajeError = '';
    this.mensajeExito = '';

    this.mostrarFormulario = true;

    this.cdr.markForCheck();
  }

  // ==========================================
  // EDITAR DISPONIBILIDAD EXISTENTE
  // ==========================================

  editarDisponibilidad(cita: Cita): void {

    if (this.guardando || this.cambiandoEstadoId !== null) {
      return;
    }

    this.editandoId = cita.disponibilidadCitaId;

    this.nuevaCita = {

      especialidadId: cita.especialidadId,

      establecimientoId: cita.establecimientoId,

      fechaAtencion: cita.fechaAtencion.substring(0, 10),

      horaInicio: cita.horaInicio.substring(0, 5),

      horaFin: cita.horaFin.substring(0, 5),

      cantidadCupos: cita.cantidadCupos

    };

    this.errorFormulario = '';
    this.mensajeError = '';
    this.mensajeExito = '';

    this.mostrarFormulario = true;

    this.cdr.markForCheck();
  }

  // ==========================================
  // CERRAR FORMULARIO
  // ==========================================

  cerrarFormulario(): void {

    if (this.guardando) {
      return;
    }

    this.mostrarFormulario = false;

    this.errorFormulario = '';

    this.editandoId = null;

    this.nuevaCita = this.crearFormularioVacio();

    this.cdr.markForCheck();
  }

  // ==========================================
  // ACTUALIZAR CANTIDAD DE CUPOS
  // ==========================================

  actualizarCantidadCupos(evento: Event): void {

    const input = evento.target as HTMLInputElement;

    const valor = input.value.trim();

    this.nuevaCita.cantidadCupos =
      valor === '' ? 0 : Number(valor);

  }

  // ==========================================
  // VALIDAR FORMULARIO
  // ==========================================

  private validarFormulario(): boolean {

    if (
      !this.nuevaCita.especialidadId ||
      !this.nuevaCita.establecimientoId ||
      !this.nuevaCita.fechaAtencion ||
      !this.nuevaCita.horaInicio ||
      !this.nuevaCita.horaFin
    ) {

      this.errorFormulario =
        'Complete todos los campos del formulario.';

      return false;
    }

    // VALIDAR FECHA

    if (this.nuevaCita.fechaAtencion < this.fechaMinima) {

      this.errorFormulario =
        'La fecha de atención debe ser a partir de mañana.';

      return false;
    }

    // VALIDAR HORARIOS

    if (this.nuevaCita.horaInicio >= this.nuevaCita.horaFin) {

      this.errorFormulario =
        'La hora de inicio debe ser anterior a la hora de finalización.';

      return false;
    }

    // VALIDAR CUPOS

    const cantidadCupos = Number(
      this.nuevaCita.cantidadCupos
    );

    if (
      !Number.isSafeInteger(cantidadCupos) ||
      cantidadCupos <= 0
    ) {

      this.errorFormulario =
        'La cantidad de cupos debe ser un número entero mayor que cero.';

      return false;
    }

    return true;
  }

  // ==========================================
  // CONSTRUIR OBJETO PARA EL BACKEND
  // ==========================================

  private construirDatos(): CrearCita {

    return {

      especialidadId: Number(
        this.nuevaCita.especialidadId
      ),

      establecimientoId: Number(
        this.nuevaCita.establecimientoId
      ),

      fechaAtencion: this.nuevaCita.fechaAtencion,

      horaInicio: this.nuevaCita.horaInicio,

      horaFin: this.nuevaCita.horaFin,

      cantidadCupos: Number(
        this.nuevaCita.cantidadCupos
      )

    };
  }

  // ==========================================
  // PUBLICAR O ACTUALIZAR DISPONIBILIDAD
  // ==========================================

  publicarCita(): void {

    if (this.guardando || this.cambiandoEstadoId !== null) {
      return;
    }

    this.errorFormulario = '';
    this.mensajeExito = '';

    // VALIDAR FORMULARIO

    if (!this.validarFormulario()) {

      this.cdr.markForCheck();

      return;
    }

    // CONSTRUIR DATOS

    const datos = this.construirDatos();

    console.log(
      'Datos enviados al backend:',
      datos
    );

    // INICIAR ENVÍO

    this.guardando = true;

    // SI EXISTE ID: ACTUALIZAR (PUT)
    // SI NO EXISTE ID: REGISTRAR (POST)

    const operacion = this.editandoId !== null
      ? this.citaService.actualizar(this.editandoId, datos)
      : this.citaService.publicar(datos);

    operacion.subscribe({

      // RESPUESTA EXITOSA

      next: cita => {

        this.guardando = false;

        const estabaEditando = this.editandoId !== null;

        this.mostrarFormulario = false;

        this.mensajeExito = estabaEditando
          ? `Disponibilidad actualizada correctamente. Registro N.° ${cita.disponibilidadCitaId}.`
          : `Disponibilidad publicada correctamente. Registro N.° ${cita.disponibilidadCitaId}.`;

        // ACTUALIZAR FILTROS

        this.especialidadId = cita.especialidadId;

        this.establecimientoId = cita.establecimientoId;

        this.fecha = cita.fechaAtencion.substring(0, 10);

        // LIMPIAR FORMULARIO

        this.editandoId = null;

        this.nuevaCita = this.crearFormularioVacio();

        // ACTUALIZAR TABLA SIN BORRAR MENSAJE DE ÉXITO

        this.consultarCitas(false);

        this.cdr.markForCheck();

      },

      // ERROR DEL BACKEND

      error: error => {

        console.error(
          'Error al guardar disponibilidad:',
          error
        );

        this.errorFormulario =
          error.error?.mensaje ??
          error.error?.message ??
          'No fue posible guardar la disponibilidad. Verifique los datos y sus permisos.';

        this.guardando = false;

        this.cdr.markForCheck();

      }

    });

  }

  // ==========================================
  // DESACTIVAR / REACTIVAR DISPONIBILIDAD
  // ==========================================

  cambiarEstado(cita: Cita): void {

    if (
      this.guardando ||
      this.cargando ||
      this.cambiandoEstadoId !== null
    ) {
      return;
    }

    const estadoActual = cita.estado.toLowerCase();

    let nuevoEstado: string;

    if (estadoActual === 'disponible') {

      nuevoEstado = 'Inactivo';

    } else if (estadoActual === 'inactivo') {

      nuevoEstado = 'Disponible';

    } else {

      this.mensajeError =
        'Esta disponibilidad no permite cambiar de estado desde esta opción.';

      this.cdr.markForCheck();

      return;
    }

    const accion = nuevoEstado === 'Inactivo'
      ? 'desactivar'
      : 'reactivar';

    const confirmar = window.confirm(
      `¿Está seguro de ${accion} la disponibilidad N.° ${cita.disponibilidadCitaId}?`
    );

    if (!confirmar) {
      return;
    }

    this.mensajeError = '';
    this.mensajeExito = '';

    this.cambiandoEstadoId = cita.disponibilidadCitaId;

    this.citaService.cambiarEstado(
      cita.disponibilidadCitaId,
      nuevoEstado
    ).subscribe({

      next: () => {

        this.cambiandoEstadoId = null;

        this.mensajeExito =
          `Disponibilidad N.° ${cita.disponibilidadCitaId} ${
            nuevoEstado === 'Inactivo'
              ? 'desactivada'
              : 'reactivada'
          } correctamente.`;

        // RECARGAR TABLA SIN BORRAR EL MENSAJE

        this.consultarCitas(false);

        this.cdr.markForCheck();

      },

      error: error => {

        console.error(
          'Error al cambiar estado de disponibilidad:',
          error
        );

        this.mensajeError =
          error.error?.mensaje ??
          error.error?.message ??
          'No fue posible cambiar el estado de la disponibilidad.';

        this.cambiandoEstadoId = null;

        this.cdr.markForCheck();

      }

    });

  }

  // ==========================================
  // FECHA DE MAÑANA
  // ==========================================

  private obtenerFechaManana(): string {

    const fecha = new Date();

    fecha.setDate(
      fecha.getDate() + 1
    );

    const anio = fecha.getFullYear();

    const mes = String(
      fecha.getMonth() + 1
    ).padStart(2, '0');

    const dia = String(
      fecha.getDate()
    ).padStart(2, '0');

    return `${anio}-${mes}-${dia}`;

  }

  // ==========================================
  // LIMPIAR FILTROS
  // ==========================================

  limpiarFiltros(): void {

    this.especialidadId = null;

    this.establecimientoId = null;

    this.fecha = '';

    this.citas = [];

    this.mensajeError = '';

    this.mensajeExito = '';

    this.cargando = false;

    this.cdr.markForCheck();

  }

  // ==========================================
  // CLASE VISUAL DEL ESTADO
  // ==========================================

  obtenerClaseEstado(estado: string): string {

    switch (estado.toLowerCase()) {

      case 'disponible':
        return 'estado-disponible';

      case 'agotado':
        return 'estado-agotado';

      case 'inactivo':
        return 'estado-inactivo';

      default:
        return '';

    }

  }

}