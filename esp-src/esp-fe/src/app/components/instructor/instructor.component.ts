import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-instructor',
  templateUrl: './instructor.component.html',
  styleUrls: ['./instructor.component.css']
})
export class InstructorComponent implements OnInit {
  signInForm: FormGroup;
  signUpForm: FormGroup;
  constructor(private formBuilder: FormBuilder) {
    this.signInForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.signUpForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmNewPassword: ['', Validators.required],
      email: [''],
      department: ['',Validators.required],
      contact_no: ['',Validators.required]
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
    }
  }

  onSignUp() {
    console.log(this.signUpForm.value)
    if (this.signUpForm.valid) {
      // const name = this.signUpForm.value.name;
      const id = this.signUpForm.value.id;
      const password = this.signUpForm.value.password;
      const newPassword = this.signUpForm.value.newPassword;
      const confirmNewPassword = this.signUpForm.value.confirmNewPassword;
      // Placeholder: Simulate authentication logic
      // this.instructorservice.SignUp(this.signUpForm.value).subscribe(
      //   data => console.log('success',data),
      // )
      console.log('Signing in with ID:', id, ', password:', password,
        ', new password:', newPassword,
        'and confirm password: ', confirmNewPassword);
    }

  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
  }
}
