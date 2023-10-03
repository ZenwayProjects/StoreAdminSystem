import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/enviroments/enviroment';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private _baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient) {}

  private getAuthHeader(): HttpHeaders {
    const token = localStorage.getItem('token');

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  addUser(shop: any): Observable<any> {
    const headers = this.getAuthHeader();

    return this.http.post<any>(`${this._baseUrl}/users`, shop, { headers });
  }
}
