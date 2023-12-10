import {Component, OnInit} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, Validators} from "@angular/forms";
import {AdminService} from 'src/app/services/admin.service';
import {Admin} from "../../System Entities/Admin";
import { Course } from 'src/app/System Entities/course';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  signInForm: FormGroup;
  admin: Admin | null
  selectedSection: number

  constructor(
    private formBuilder: NonNullableFormBuilder,
    private service:AdminService
  ) {

    this.signInForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.admin = null
    this.selectedSection = 0

  }

  ngOnInit() {
    // Any initialization logic
  }

  async onSubmit() {
    if (this.signInForm.valid) {
      const id = this.signInForm.value.id;
      const password = this.signInForm.value.password;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, 'and password:', password);

      let isSuccess: boolean | null = await this.service.signIn(id, password);

      if (isSuccess) {
        alert("The admin has been signIn successfully")
        this.admin = new Admin("", "", "", "")
      } else {
        alert("The ID or the password is not correct")
      }

    }
  }

  selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
  }

  async addCourse () {

    // initialize the course with this constructor.
    // inforce in the form that the courseCode has max 7 characters
    // and Course name max 40 character
    // and department id and creditHours are in the range of the byte [-128,127]
    let newCourse = new Course("CSEN 901", "Software Engineering", "This course is about software engineering", 1, 3, [])



    let isSuccess: boolean | null = await this.service.addCourse(newCourse)

    if(isSuccess){
      alert("The course has been added successfully")
    }
    else{
      alert("The course has not been added successfully")
    }
  }

}
