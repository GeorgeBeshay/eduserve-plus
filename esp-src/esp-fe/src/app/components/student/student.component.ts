import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StudentService} from 'src/app/services/student.service';
import {Student} from "../../System Entities/Student";
import Swal from "sweetalert2";

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit{
  student: Student | null;
  selectedSection: number;

  signInForm: FormGroup;
  signUpForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private service:StudentService) {
    this.student = null;
    this.selectedSection = 0;

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
      email: ['', [Validators.required, Validators.email]],
      gender: ['', Validators.required]
    });
  }

  ngOnInit() {
    // Restoring cached object.
    let tempObj = sessionStorage.getItem("studentObject");
    if(tempObj != null)
      this.student = JSON.parse(tempObj);
  }

  selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
  }

  async onSubmit() {
    if (this.signInForm.valid) {
      const id = this.signInForm.value.id;
      const password = this.signInForm.value.password;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, 'and password:', password);

      let isSuccess: boolean | null = await this.service.signIn(id, password);

      if (isSuccess) {

        await Swal.fire({
          position: "center",
          icon: "success",
          title: "Successful",
          text: "Student Sign In Process is Successful.",
          showConfirmButton: false,
          timer: 2000
        });

        this.student = new Student(id, "", "", "", "", "", "", "", "", "", "", "");

        // caching object.
        sessionStorage.setItem("studentObject", JSON.stringify(this.student));

      } else {

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "The Given ID or Password is Incorrect.",
        });

      }

    }
  }

  async onSignUp() {
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
      let gender = this.signUpForm.value.gender

      if (gender === "male") {
        gender = "true"
      }
      else {
        gender = "false"
      }

      let student = new Student(id, "", "", "", fullName, ssn, dateOfBirth, address, phone, landline, gender, email)

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
        let isSuccess: boolean | null = await this.service.signUp(student, password, newPassword);

      if (isSuccess) {

        await Swal.fire({
          position: "center",
          icon: "success",
          title: "Successful",
          text: "Student Sign Up Process is Successful.",
          showConfirmButton: false,
          timer: 2000
        });

        this.student = student;

      } else {

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Student Sign Up Process has failed.",
        });

      }
    }


  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
  }
}
