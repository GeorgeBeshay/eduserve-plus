import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import {Admin} from "../System Entities/Admin";
import { Course } from '../System Entities/course';
import { Instructor } from '../System Entities/Instructor';
import { Student } from '../System Entities/Student';
import { UnregisteredStudent } from '../System Entities/unregisterStudent';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  URL = 'http://localhost:8081/esp-server/admin-endpoint/'

  constructor(private http: HttpClient) { }

  // signIn(adminId:string, adminPwHash:string){
  //   return this._http.get<boolean>(`${this.signInurl}`, new Admin(adminId, adminPwHash, "", ""), {responseType:'json'});
  // }

  // Sign In Request (either the full user data will return or not, in case of unsuccessful authentication )
  async signIn(adminId: string, password: string){
    let admin = new Admin(adminId, "", "", "")
    try {
      return await firstValueFrom(
        this.http.post<boolean>(this.URL + 'signIn', {"admin": admin, "password": password}, {responseType:'json'})   // returns the user object.
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return null;
  }

  async createAdmin(admin: Admin, adminPw: string){
    try {
      return await firstValueFrom(
        this.http.post<boolean>(this.URL + 'addNewAdmin', {"admin": admin, "adminPw": adminPw}, {responseType:'json'})   // returns the user object.
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return null;
  }

  async addCourse(newCourse: Course){
    try {
      // return response entity.
      // in the body of the response entity, there is a boolean value.
      // if added successfully return true.
      // else return false.
      return await firstValueFrom(
        this.http.post<boolean>(this.URL + 'addCourse', newCourse, {responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return null;
  }

  async uploadUnregisteredStudents(file: File) {

    const formData: FormData = new FormData();
    formData.append('unregisteredStudents', file);

    try {
      return await firstValueFrom (
        this.http.post<{"studentsSuccessfullyAdded": number, "failedStudentsToBeAdded": number[]}>(this.URL + 'addUnregisteredStudents', formData)
      );
    }

    catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return {"studentsSuccessfullyAdded": 0, "failedStudentsToBeAdded": []};

  }

  async getAllCourses(): Promise<Course[]> {
    try {
      return await firstValueFrom(
        this.http.post<Course[]>(this.URL + 'getAllCourses', {}, {responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return [];
  }

  async getAllUnregisteredInstructors(): Promise<Instructor[]> {
    try {
      return await firstValueFrom(
        this.http.post<Instructor[]>(this.URL + 'getAllUnregisteredInstructors', {}, {responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return [];
  }

  async getAllUnregisteredStudents(): Promise<Student[]> {
    try {
      return await firstValueFrom(
        this.http.post<Student[]>(this.URL + 'getAllUnregisteredStudents', {}, {responseType:'json'})
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return [];
  }

}
