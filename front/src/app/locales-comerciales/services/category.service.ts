import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from 'src/enviroments/enviroment';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private _baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient) {}

  private getAuthHeader(): HttpHeaders {
    const token = localStorage.getItem('token');

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  loadCategories(): Observable<any[]> {
    const headers = this.getAuthHeader();

    return this.http
      .get<any[]>(`${this._baseUrl}/api/categoria/listar`, { headers })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }
}
