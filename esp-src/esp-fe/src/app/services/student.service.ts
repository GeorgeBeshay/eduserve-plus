import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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

export class Student{

  studentId: string
  departmentId: string
  studentLevel: string
  gpa: string
  studentName: string
  ssn: string
  bdate: string
  studentAddress: string
  phone: string
  landline: string
  gender: string
  email: string

    constructor(studentId: string, departmentId: string, studentLevel: string, gpa: string,
      studentName: string, ssn: string, bdate: string, studentAddress: string, phone: string,
      landline: string, gender: string, email: string){

        this.studentId = studentId
        this.departmentId = departmentId
        this.studentLevel = studentLevel
        this.gpa = gpa
        this.studentName = studentName
        this.ssn = ssn
        this.bdate = bdate
        this.studentAddress = studentAddress
        this.phone = phone
        this.landline = landline
        this.gender = gender
        this.email = email

    }
}
