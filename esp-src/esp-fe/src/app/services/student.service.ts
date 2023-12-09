import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Student} from "../System Entities/Student";
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  URL = 'http://localhost:8081/esp-server/student-endpoint/'

  constructor(private http:HttpClient) { }

  async signIn(studentId:string, password:string) {

    try {
      return await firstValueFrom (
        this.http.post<boolean>(this.URL + 'signIn', {"student": {"studentId": studentId}, "password": password}, {responseType:'json'})   // returns the user object.
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return null;
  }

  async signUp(student: Student, OTPPassword: string, password: string) {

    console.log("in service sign up");

    try {
      return await firstValueFrom (
        this.http.post<boolean>(this.URL + 'signUp', {"student": student, "password": password, "OTPPassword": OTPPassword}, {responseType:'json'})
      );
    }

    catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return null;
  }

}
