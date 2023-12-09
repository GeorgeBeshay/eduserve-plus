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

  signUp(student:Student){
    return this._http.post<boolean>(`${this.signUpurl}`,student,{responseType:'json'});
  }

}

export class Student{

  studentId: string
  studentPwHash: string
  departmentId: string
  studentLevel: string
  gpa: string
  studentName: string
  ssn: string
  birthDate: string
  studentAddress: string
  phone: string
  landline: string
  gender: string
  email: string

    constructor(studentId: string, studentPwHash: string, departmentId: string, studentLevel: string, gpa: string,
       studentName: string, ssn: string, birthDate: string, studentAddress: string, phone: string,
       landline: string, gender: string, email: string){

        this.studentId = studentId
        this.studentPwHash = studentPwHash
        this.departmentId = departmentId
        this.studentLevel = studentLevel
        this.gpa = gpa
        this.studentName = studentName
        this.ssn = ssn
        this.birthDate = birthDate
        this.studentAddress = studentAddress
        this.phone = phone
        this.landline = landline
        this.gender = gender
        this.email = email

    }
}
