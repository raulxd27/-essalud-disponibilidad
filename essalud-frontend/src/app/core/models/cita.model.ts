export interface Cita {
  disponibilidadCitaId: number;
  especialidadId: number;
  nombreEspecialidad: string;
  establecimientoId: number;
  nombreEstablecimiento: string;
  fechaAtencion: string;
  horaInicio: string;
  horaFin: string;
  cantidadCupos: number;
  estado: string;
  fechaActualizacion: string;
}

export interface CrearCita {
  especialidadId: number;
  establecimientoId: number;
  fechaAtencion: string;
  horaInicio: string;
  horaFin: string;
  cantidadCupos: number;
}