import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Student } from './student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private _http: HttpClient) { }

  url = ''

  signUp(studentForm: Student){
    return this._http.post<any>(this.url,studentForm);
  }
}
