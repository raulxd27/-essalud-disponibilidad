import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Especialidad } from '../models/especialidad.model';

@Injectable({
  providedIn: 'root'
})
export class EspecialidadService {

  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/especialidades';

  listar(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(this.apiUrl);
  }
}