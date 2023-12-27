import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Instructor} from "../../System Entities/Instructor";
import { InstructorService } from 'src/app/services/instructor.service';
import Swal from "sweetalert2";

@Component({
  selector: 'app-instructor',
  templateUrl: './instructor.component.html',
  styleUrls: ['./instructor.component.css']
})
export class InstructorComponent implements OnInit {
  signInForm: FormGroup;
  signUpForm: FormGroup;
  constructor(private formBuilder: FormBuilder, private service:InstructorService) {
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
    let tempObj = sessionStorage.getItem("instructorObj");
    // TODO: uncomment the following upon creating the instructor object.
    // if(tempObj != null)
      // this.instructor = JSON.parse(tempObj);
  }

  async onSubmit() {
    if (this.signInForm.valid) {
      const id = this.signInForm.value.id;
      const password = this.signInForm.value.password;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, 'and password:', password);
      let isSuccess:boolean | null = await this.service.signIn(id,password);
      if(isSuccess){

        await Swal.fire({
          position: "center",
          icon: "success",
          title: "Successful",
          text: "Instructor Sign In Process is Successful.",
          showConfirmButton: false,
          timer: 2000
        });

      } else {

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "The Given ID or Password is Incorrect.",
        });

      }
      // call API
    }
  }

  async onSignUp() {
    console.log(this.signUpForm.value)
    if (this.signUpForm.valid) {

      // const name = this.signUpForm.value.name;
      const id = this.signUpForm.value.id;
      const password = this.signUpForm.value.password;
      const newPassword = this.signUpForm.value.newPassword;
      const confirmNewPassword = this.signUpForm.value.confirmNewPassword;
      const dptId = this.signUpForm.value.department;
      const instructorName = this.signUpForm.value.instructorName;
      const intstructorPhone = this.signUpForm.value.contact_no;
      const instructorEmail = this.signUpForm.value.email;
      const officeHours = this.signUpForm.value.officeHours;
      let instructor = new Instructor(id,"",dptId,instructorName,intstructorPhone,instructorEmail,officeHours);
      console.log('Signing in with ID:', id, ', password:', password,
        ', new password:', newPassword,
        'and confirm password: ', confirmNewPassword);

      let isSuccess: boolean | null = await this.service.signUp(newPassword,password,instructor);
      if(isSuccess){

        await Swal.fire({
          position: "center",
          icon: "success",
          title: "Successful",
          text: "Instructor Sign Up Process is Successful.",
          showConfirmButton: false,
          timer: 2000
        });

      }
      else{

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Instructor Sign Up Process has failed.",
        });

      }
    }

  }

  onTabChanged(event: number) {
    console.log('Tab changed to index:', event);
  }
}
