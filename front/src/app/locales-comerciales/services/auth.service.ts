import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { AuthStatus } from '../interfaces';
import { environment } from 'src/enviroments/enviroment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly baseUrl: string = environment.baseUrl;

  private _currentUser: any | null = null;
  private _authStatus: AuthStatus = AuthStatus.checking;

  public get currentUser(): any | null {
    return this._currentUser;
  }

  public get authStatus(): AuthStatus {
    return this._authStatus;
  }

  constructor(private http: HttpClient) {}

  private setAuthentication(user: any, token: string): boolean {
    this._currentUser = user;
    this._authStatus = AuthStatus.authenticated;
    localStorage.setItem('token', token);

    return true;
  }

  login(login: string, password: string): Observable<any> {
    const url = `${this.baseUrl}/api/auth/login`;

    const body = { login, password };

    return this.http.post<any>(url, body).pipe(
      map(({ Login, token }) => this.setAuthentication(Login, token)),
      catchError((err) => throwError(err.error.message))
    );
  }

  logout() {
    localStorage.clear();
  }
}
