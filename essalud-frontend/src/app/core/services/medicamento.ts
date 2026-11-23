import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Medicamento } from '../models/medicamento.model';

@Injectable({
  providedIn: 'root'
})
export class MedicamentoService {

  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/medicamentos';

  listar(nombre?: string): Observable<Medicamento[]> {

    let params = new HttpParams();

    if (nombre && nombre.trim()) {
      params = params.set('nombre', nombre.trim());
    }

    return this.http.get<Medicamento[]>(this.apiUrl, { params });
  }
}