import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Student} from "../System Entities/Student";
import { Course } from './../System Entities/Course';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';

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
  async loadAvailableCoursesForRegistration(studentId: any) {
    try{
      return await firstValueFrom(
      this.http.post<Course[]>(this.URL+'getAvailableCourses', Number(studentId),{responseType:'json'})
    );
  }catch(error){
    if(error instanceof HttpErrorResponse){
      console.error('Bad request');
    }
    else{
      console.error('Error');
    }
    return[];
  }
}

  async getStudentEnrolledCourses(studentId: string) {

    try {
      return await firstValueFrom (
        this.http.post<Course[]>(this.URL + 'getStudentEnrolledCourses', Number(studentId), {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return [];
  }

  async withdrawCourses(studentId: string, courses: Course[]) {

    try {
      return await firstValueFrom (
        this.http.post<boolean>(this.URL + 'withdrawCourses', {"studentId": Number(studentId), "courses": courses}, {responseType:'json'})
      );

    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');

      else
        console.error('Error');
    }
    return false;
  }
  async registerCourses(studentId: string | undefined, selectedCourses: Course[]) {
    try {
      return await firstValueFrom(
        this.http.post<boolean>(this.URL + 'registerCourses',{"studentId": Number(studentId),"courses:":selectedCourses},{responseType:'json'})
      );
    }catch (error){
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
      }
      return false;
  }



}
