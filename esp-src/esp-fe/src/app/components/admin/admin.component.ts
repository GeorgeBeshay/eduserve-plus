import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatTabChangeEvent} from "@angular/material/tabs";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  signInForm: FormGroup;
  signUpForm: FormGroup;
  selectedIndex = 0; // Variable to control the selected tab index

  constructor(private formBuilder: FormBuilder) {
    this.signInForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.signUpForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
    // Any initialization logic
  }

  onSubmit() {
    if (this.signInForm.valid) {
      // Perform sign-in logic using this.authForm.value.email and this.authForm.value.password
    }
  }

  onSignUp() {
    // Redirect to sign-up page or implement sign-up logic
  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
    // Perform actions based on the selected tab index, if needed
  }
}
