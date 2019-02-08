import {ElementRef, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserService} from './user.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient, private userService: UserService) {
  }

  upload(filename: string, file: File): Observable<any> {
    const body = new FormData();
    const params = new HttpParams();
    const headers = this.userService.getAuthorizationHeader();
    headers.set('Content-Type', null);
    headers.set('Accept', 'multipart/form-data');

    body.append('file', file, filename);
    return this.userService.authPost('http://localhost:8080/file', body, headers);
  }

  delete(filename: string): Observable<any> {
    return this.userService.authDelete('http://localhost:8080/file/' + filename);
  }

  load(filename: string, link: ElementRef) {
    this.http.get('http://localhost:8080/file/' + filename, {
      headers: this.userService.getAuthorizationHeader(),
      observe: 'response', responseType: 'blob'
    }).subscribe((value) => this.onResponse(filename, value.body, link));
    // this.userService.authGet<Blob>('http://localhost:8080/file/' + filename, null, 'blob' as 'json')
  }

  onResponse(filename: string, data, link) {
    const blob = new Blob([data], {type: 'text/csv'});
    const url = window.URL.createObjectURL(blob);

    // const link = ;
    link.nativeElement.href = url;
    link.nativeElement.download = filename.slice(filename.indexOf('/') + 1);
    link.nativeElement.click();

    window.URL.revokeObjectURL(url);
    // window.open(url);
  }
}
