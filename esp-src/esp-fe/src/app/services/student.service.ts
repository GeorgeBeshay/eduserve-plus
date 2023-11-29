import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  loginurl = 'http://localhost:8080/student/login'
  signupurl = 'http://localhost:8080/student/signup'
  constructor(private _http:HttpClient) { }

  signIn(ID:string, password:string){
    return this._http.post<boolean>(`${this.loginurl}`,{ID,password},{responseType:'json'});
  }

  signUp(student:Student){
    return this._http.post<boolean>(`${this.signupurl}`,student,{responseType:'json'});
  }

}

export class Student{
    
    ID:string
    password:string
    newPassword:string
    confirmNewPassword:string
    fullName:string
    SSN:string
    dateOfBirth:string
    address:string
    phone:string 
    landline:string
    email:string 
    gender:string 
    constructor(ID:string,password:string,newPassword:string,confirmNewPassword:string,fullName:string,SSN:string,dateOfBirth:string,address:string,phone:string,landline:string,email:string,gender:string){
      this.ID = ID
      this.password = password
      this.newPassword = newPassword
      this.confirmNewPassword = confirmNewPassword
      this.fullName = fullName
      this.SSN = SSN
      this.dateOfBirth = dateOfBirth
      this.address = address
      this.phone = phone
      this.landline = landline
      this.email = email
      this.gender = gender
      
    }
}
