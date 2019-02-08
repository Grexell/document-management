import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {UserService} from './user.service';
import {Observable} from 'rxjs';
import {Access} from '../model/access';

@Injectable({
  providedIn: 'root'
})
export class AccessService {

  constructor(private http: HttpClient, private router: Router, private userService: UserService) { }

  getAccesses(): Observable<Access[]> {
    return this.http.get<Access[]>('http://localhost:8080/accesses', { headers: this.userService.getAuthorizationHeader()});
  }
}
