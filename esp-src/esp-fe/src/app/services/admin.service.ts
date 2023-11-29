import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  loginurl = 'http://localhost:8080/admin/login'
  signupurl = 'http://localhost:8080/admin/signup'
  constructor(private _http:HttpClient) { }

  signIn(ID:string, password:string){
    return this._http.post<boolean>(`${this.loginurl}`,{ID,password},{responseType:'json'});
  }

  signUp(ID:string, password:string, newPassword:string, confirmNewPassword:string){
    return this._http.post<boolean>(`${this.signupurl}`,{ID,password,newPassword,confirmNewPassword},{responseType:'json'});
  }

}

export class Admin{
  
}