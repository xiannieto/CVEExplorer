import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QueryDTO } from '../models/queryDTO.model';
import { QueryResultsDTO } from '../models/queryResultDTO.model';

@Injectable({
  providedIn: 'root'
})
export class QueryService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  search(queryDTO: QueryDTO): Observable<QueryResultsDTO> {
    return this.http.post<QueryResultsDTO>(`${this.apiUrl}/search`, queryDTO);
  }

  getQueryCwes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/getQueryCwes`);
  }

}
