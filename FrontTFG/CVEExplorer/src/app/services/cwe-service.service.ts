import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, of, tap, throwError } from 'rxjs';
import { CWE } from '../models/cwe.model';

interface CweListResponse {
  cwes: CWE[];
  totalResults: number;
}

@Injectable({
  providedIn: 'root',
})
export class CweService {
  private cweUrl = 'http://localhost:8080/api/cwes';

  constructor(private http: HttpClient) {}

  getCWEById(id: string): Observable<CWE> {
    return this.http.get<CWE>(`${this.cweUrl}/${id}`).pipe(
      tap((data) => console.log(data)),
      catchError(this.handleError)
    );
  }

  getAncestors(id: string): Observable<CWE[]> {
    return this.http.get<CWE[]>(`${this.cweUrl}/${id}/ancestors`).pipe(
      catchError((error) => {
        // No mostrar un mensaje de error cuando el servidor devuelva un código de estado HTTP 404
        console.error('Error al obtener los padres del CWE:', error);
        return of([]);
      })
    );
  }

  getChildren(id: string): Observable<CWE[]> {
    return this.http.get<CWE[]>(`${this.cweUrl}/${id}/getChildren`).pipe(
      catchError((error) => {
        console.error('Error al obtener los hijos del CWE:', error);
        return of([]);
      })
    );
  }

  findRoots(
    pageNumber: number = 0,
    pageSize: number = 10
  ): Observable<CweListResponse> {
    return this.http
      .get<CweListResponse>(
        `${this.cweUrl}/roots/?pageNumber=${pageNumber}&pageSize=${pageSize}`
      )
      .pipe(catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error(
        `Backend returned code ${error.status}, body was: `,
        error.error
      );
    }
    return throwError(
      () => new Error('Something bad happened; please try again later.')
    );
  }
}
