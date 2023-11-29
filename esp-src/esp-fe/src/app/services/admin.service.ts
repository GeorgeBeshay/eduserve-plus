import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  signInurl = 'http://localhost:8081/esp-server/admin-controller/signIn'
  signUpurl = 'http://localhost:8081/esp-server/admin-controller/signUp'
  
  constructor(private _http:HttpClient) { }

  signIn(ID:string, password:string){
    return this._http.post<boolean>(`${this.signInurl}`,{ID,password},{responseType:'json'});
  }

  signUp(ID:string, password:string, newPassword:string, confirmNewPassword:string){
    return this._http.post<boolean>(`${this.signUpurl}`,{ID,password,newPassword,confirmNewPassword},{responseType:'json'});
  }

}

export class Admin{

  adminId: string
  adminPwHash: string
  adminName: string
  creatorAdminId: string

  constructor(adminId: string, adminPwHash: string, adminName: string, creatorAdminId: string) {
    this.adminId = adminId
    this.adminName = adminName
    this.adminPwHash = adminPwHash
    this.creatorAdminId = creatorAdminId
  }
}
