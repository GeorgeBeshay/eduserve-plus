import { Component } from '@angular/core';
import { Admin } from './admin';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin-sign-in',
  templateUrl: './admin-sign-in.component.html',
  styleUrls: ['./admin-sign-in.component.css']
})
export class AdminSignInComponent {

  constructor(private adminService: AdminService){}
  form = new Admin('','')
  onSubmit() {
    console.log(this.form)
    this.adminService.signIn(this.form)
    .subscribe(
      data => console.log('success', data),
      error => console.log('error',error)
    )
  }

}
