import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Instructor } from '../System Entities/Instructor';
import { firstValueFrom, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InstructorService {

  URL = 'http://localhost:8081/esp-server/instructor-endpoint/'

  constructor(private http:HttpClient) { }

  async signIn(instructorId: string, password: string){
    let instructor  = new Instructor(instructorId, "", "", "","","","")
    try {
      return await firstValueFrom(
        this.http.post<boolean>(this.URL+'signIn',{"instructor":instructor,"password":password})   // returns the user object.
      );
    } catch (error) {
      if(error instanceof HttpErrorResponse)
        console.error('Bad request');
      else
        console.error('Error');
    }
    return null;
  }
  async signUp(newPassword:string, password:string, instructor:Instructor){
    try {
      return await firstValueFrom(
        this.http.post<boolean>(this.URL+'signUp',{"instructor":instructor,"OTPPassword":password,"newPassword":newPassword},{responseType:'json'})   // returns the user object.
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

