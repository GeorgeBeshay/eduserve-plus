import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { Student, StudentService } from 'src/app/services/student.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit{
  signInForm: FormGroup;
  signUpForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private service:StudentService) {
    this.signInForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.signUpForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmNewPassword: ['', Validators.required],
      fullName: ['', Validators.required],
      ssn: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', Validators.required],
      landline: ['', Validators.required],
      email: ['', Validators.required],
      gender: ['', Validators.required]
    });
  }

  ngOnInit() {
    // Any initialization logic
  }

  onSubmit() {
    if (this.signInForm.valid) {
      const id = this.signInForm.value.id;
      const password = this.signInForm.value.password;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, 'and password:', password);

      // call API
      this.service.signIn(id,password)
      .subscribe((body) => {
        alert(body)
      })

    }
  }

  onSignUp() {
    if (this.signUpForm.valid) {
      const id = this.signUpForm.value.id
      const password = this.signUpForm.value.password;
      const newPassword = this.signUpForm.value.newPassword;
      const confirmNewPassword = this.signUpForm.value.confirmNewPassword;
      const fullName = this.signUpForm.value.fullName
      const ssn = this.signUpForm.value.ssn
      const dateOfBirth = this.signUpForm.value.dateOfBirth
      const address = this.signUpForm.value.address
      const phone = this.signUpForm.value.phone
      const landline = this.signUpForm.value.landline
      const email = this.signUpForm.value.email
      const gender = this.signUpForm.value.gender

      const student = new Student(id,password,newPassword,confirmNewPassword,fullName,ssn,dateOfBirth,address,phone,landline,email,gender)

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, ', password:', password,
        ', new password:', newPassword,
        ', confirm password: ', confirmNewPassword,
        ', fullName: ', fullName,
        ', snn: ', ssn,
        ', dateOfBirth: ', dateOfBirth,
        ', address: ', address,
        ', phone: ', phone,
        ', landline: ', landline,
        ', email: ', email,
        ', gender: ', gender);

        //cal API
        console.log("hi")
        console.log(student)
        this.service.signUp(student)
        .subscribe((body) => {
          alert(body)
        })
    }

    
  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
  }
}
