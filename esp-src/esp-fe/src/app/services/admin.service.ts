import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import {Admin} from "../System Entities/Admin";
import { Course } from '../System Entities/course';

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



}
