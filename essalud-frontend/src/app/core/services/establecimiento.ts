import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Establecimiento } from '../models/establecimiento.model';

@Injectable({
  providedIn: 'root'
})
export class EstablecimientoService {

  private http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/establecimientos';

  listar(): Observable<Establecimiento[]> {
    return this.http.get<Establecimiento[]>(this.apiUrl);
  }
}