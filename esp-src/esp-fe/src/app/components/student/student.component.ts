import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { StudentService } from './student.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit{
  signInForm: FormGroup;
  signUpForm: FormGroup;
  form = new Student('','','','','','','','','','','');

  signup() {
    console.log(this.form)
    this.studentService.signUp(this.form)
    .subscribe(
      data => console.log('success', data),
      error => console.log('error',error)
  )
}

  constructor(private formBuilder: FormBuilder,private studentService: StudentService) {
    this.signInForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required]
    });

    
    
    

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

  onSubmit() {
  }

  onSignUp() {
  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
  }
}

export class Student {
  constructor(
      public id: string,
      public password: string,
      public newPassword: string,
      public name: string,
      public ssn: string,
      public date: any,
      public address: string,
      public phone_number: string,
      public landline: string,
      public gender: string,
      public Email: string
  ){}
}
