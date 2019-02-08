import {Injectable} from '@angular/core';
import {StorageService} from './storage.service';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {Observable, of} from 'rxjs';
import {Router} from '@angular/router';
import * as jwt_decode from 'jwt-decode';

const TOKEN_KEY = 'token';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private authenticated = false;
  private token: any;
  private decodedToken: any;
  private tokenUri = 'http://localhost:9999/oauth/token';
  private clientId = 'resourceServer';
  private clientSecret = 'resourceSecret';

  constructor(private storage: StorageService, private http: HttpClient, private router: Router) {
    this.token = this.storage.getValue(TOKEN_KEY);
    if (this.token) {
      this.decodedToken = this.getDecodedAccessToken();
      if (this.decodedToken['expires_in'] * 1000 >= new Date().getTime()) {
        this.refreshToken();
      }
      this.setAuthenticated(true);
    }
  }

  setAuthenticated(authenticated: boolean) {
    this.authenticated = authenticated;
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  getAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders('Authorization: Bearer ' + this.getAccessToken());
  }

  getAccessToken() {
    return this.token['access_token'];
  }

  getDecodedAccessToken() {
    try {
      return jwt_decode(this.getAccessToken());
    } catch (Error) {
      return null;
    }
  }

  refreshToken() {
    return this.http.post(this.tokenUri, null, {
      headers: new HttpHeaders('Authorization: Basic ' + btoa(this.clientId + ':' + this.clientSecret)),
      params: {
        'grant_type': 'refresh_token',
        'refresh_token': this.token['refresh_token'],
      }
    }).pipe(
      map(value => {
        this.token = value;
        this.storage.setValue(TOKEN_KEY, this.token);
        this.setAuthenticated(true);
        return value;
      }),
      catchError((err, data) => {
        this.setAuthenticated(false);
        this.router.navigate(['/login']);
        return of(null);
      })
    );
  }

  checkAuthenticated() {
    this.setAuthenticated(true);
  }

  authenticate(login: string, password: string) {
    return this.http.post(this.tokenUri, null, {
      headers: new HttpHeaders('Authorization: Basic ' + btoa(this.clientId + ':' + this.clientSecret)),
      params: {
        'grant_type': 'password',
        'client_id': this.clientId,
        username: login,
        password: password
      }
    }).pipe(
      map(value => {
        this.token = value;
        this.storage.setValue(TOKEN_KEY, this.token);
        this.setAuthenticated(true);
        return value;
      }),
      catchError((err, data) => {
        this.setAuthenticated(false);
        return of(null);
      })
    );
  }

  logout() {
    this.storage.remove(TOKEN_KEY);
    this.setAuthenticated(false);
  }

  authGet<T>(path: string, headers?: HttpHeaders, responseType?: any): Observable<T> {
    if (headers) {
      headers.append('Authorization', ' Bearer ' + this.getAccessToken());
    } else {
      headers = this.getAuthorizationHeader();
    }
    return this.http.get<T>(path, {headers, responseType});
  }

  authPost<T>(path: string, body?: any, headers?: HttpHeaders): Observable<T> {
    if (headers) {
      headers.append('Authorization', ' Bearer ' + this.getAccessToken());
    } else {
      headers = this.getAuthorizationHeader();
    }
    return this.http.post<T>(path, body, {headers});
  }

  authPut<T>(path: string, body?: any, headers?: HttpHeaders): Observable<T> {
    if (headers) {
      headers.append('Authorization', ' Bearer ' + this.getAccessToken());
    } else {
      headers = this.getAuthorizationHeader();
    }
    return this.http.put<T>(path, body, {headers})/*.pipe(
      catchError((err, caught) => {
        this.refreshToken();
        return caught;
      })
    )*/;
  }

  authDelete<T>(path: string, headers?: HttpHeaders): Observable<T> {
    if (headers) {
      headers.append('Authorization', ' Bearer ' + this.getAccessToken());
    } else {
      headers = this.getAuthorizationHeader();
    }
    return this.http.delete<T>(path, {headers});
  }
}
