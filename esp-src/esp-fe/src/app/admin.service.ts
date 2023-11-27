import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Admin } from './admin-sign-in/admin';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  url = ''
  constructor(private _http:HttpClient) { }

  signIn(admin: Admin){
    return this._http.post<any>(this.url,admin);
  }

}
