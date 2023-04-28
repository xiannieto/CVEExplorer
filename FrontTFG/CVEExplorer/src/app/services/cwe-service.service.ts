import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, of, throwError } from 'rxjs';
import { CWE } from '../models/cwe.model';

@Injectable({
  providedIn: 'root',
})
export class CweService {
  private cweUrl = 'http://localhost:8080/api/cwes';

  constructor(private http: HttpClient) {}

  getCWEById(id: string): Observable<CWE> {
    return this.http.get<CWE>(`${this.cweUrl}/${id}`)
    .pipe(catchError(this.handleError));
  }

  getAncestors(id: string): Observable<CWE[]> {
    return this.http.get<CWE[]>(`${this.cweUrl}/${id}/ancestors`)
    .pipe(
      catchError(error => {
        console.error('Error al obtener los hijos del CWE:', error);
        return of([]);
      })
    );
  }

  findRoots(): Observable<CWE[]> {
    return this.http.get<CWE[]>(`${this.cweUrl}/roots`)
    .pipe(catchError(this.handleError));
  }

  getChildren(id: string): Observable<CWE[]> {
    return this.http.get<CWE[]>(`${this.cweUrl}/${id}/getChildren`)
    .pipe(
      catchError(error => {
        console.error('Error al obtener los hijos del CWE:', error);
        return of([]);
      })
    );
  }

  handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `,
        error.error
      );
    }
    // Return an observable with a user-facing error message.
    return throwError(
      () => new Error('Something bad happened; please try again later.')
    );
  }
}
