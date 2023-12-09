import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Student} from "../System Entities/Student";

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  signInurl = 'http://localhost:8081/esp-server/student-controller/signIn'
  signUpurl = 'http://localhost:8081/esp-server/student-controller/signUp'

  constructor(private _http:HttpClient) { }

  signIn(studentId:string, studentPwHash:string){
    return this._http.post<boolean>(`${this.signInurl}`,{studentId,studentPwHash},{responseType:'json'});
  }

  signUp(student:Student){
    return this._http.post<boolean>(`${this.signUpurl}`,student,{responseType:'json'});
  }

}

