import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserService} from './user.service';
import {Document} from '../model/document';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {

  constructor(private http: HttpClient, private userService: UserService) {
  }

  userList(): Observable<string[]> {
    return this.userService.authGet('http://localhost:8080/signs/users');
  }

  create(document: Document): Observable<any> {
    return this.userService.authPost('http://localhost:8080/documents/', document);
  }

  archive(document: Document): Observable<any> {
    return this.userService.authPost('http://localhost:8080/archive/' + document.id);
  }

  sign(document: Document): Observable<any> {
    return this.userService.authPost('http://localhost:8080/signs/' + document.id);
  }

  delete(document: Document): Observable<any> {
    return this.userService.authDelete('http://localhost:8080/documents/' + document.id);
  }

  getMy(): Observable<Document[]> {
    return this.userService.authGet<Document[]>('http://localhost:8080/documents/me');
  }

  getSign(): Observable<Document[]> {
    return this.userService.authGet<Document[]>('http://localhost:8080/signs');
  }

  getActive(): Observable<Document[]> {
    return this.userService.authGet<Document[]>('http://localhost:8080/documents/active');
  }

  getArchived(): Observable<Document[]> {
    return this.userService.authGet<Document[]>('http://localhost:8080/archive');
  }
}
