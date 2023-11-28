import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  // signInForm: FormGroup;
  signUpForm: FormGroup;

  signinform = new Admin('','')

  constructor(private formBuilder: FormBuilder,private adminService: AdminService) {
    this.signUpForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmNewPassword: ['', Validators.required]
    });
  }

  ngOnInit() {
    // Any initialization logic
  }

  SignIn() {
    console.log(this.signinform)
    this.adminService.signIn(this.signinform)
    .subscribe(
      data => console.log('success', data),
      error => console.log('error',error)
    )
  }

  onSignUp() {
    if (this.signUpForm.valid) {
      const id = this.signUpForm.value.id
      const password = this.signUpForm.value.password;
      const newPassword = this.signUpForm.value.newPassword;
      const confirmNewPassword = this.signUpForm.value.confirmNewPassword;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, ', password:', password,
        ', new password:', newPassword,
        'and confirm password: ', confirmNewPassword);
    }
  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
    // Perform actions based on the selected tab index, if needed
  }
}

export class Admin {
  constructor(
      public username: string,
      public password: string
  ){}
}
