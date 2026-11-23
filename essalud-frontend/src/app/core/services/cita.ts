import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Cita, CrearCita } from '../models/cita.model';

@Injectable({
  providedIn: 'root'
})
export class CitaService {

  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/citas';

  // CONSULTAR DISPONIBILIDAD
  listar(
    especialidadId?: number,
    establecimientoId?: number,
    fecha?: string
  ): Observable<Cita[]> {

    let params = new HttpParams();

    if (especialidadId) {
      params = params.set('especialidadId', especialidadId);
    }

    if (establecimientoId) {
      params = params.set('establecimientoId', establecimientoId);
    }

    if (fecha) {
      params = params.set('fecha', fecha);
    }

    return this.http.get<Cita[]>(this.apiUrl, { params });
  }

  // PUBLICAR DISPONIBILIDAD
  publicar(datos: CrearCita): Observable<Cita> {
    return this.http.post<Cita>(this.apiUrl, datos);
  }

  // MODIFICAR DISPONIBILIDAD
  actualizar(id: number, datos: CrearCita): Observable<Cita> {
    return this.http.put<Cita>(`${this.apiUrl}/${id}`, datos);
  }

  // CAMBIAR ESTADO
  cambiarEstado(id: number, estado: string): Observable<Cita> {
    return this.http.patch<Cita>(
      `${this.apiUrl}/${id}/estado`,
      { estado }
    );
  }
}