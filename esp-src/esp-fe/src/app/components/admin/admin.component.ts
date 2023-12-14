import {Component, OnInit} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, Validators} from "@angular/forms";
import {AdminService} from '../../services/admin.service';
import {Admin} from "../../System Entities/Admin";
import { Course } from 'src/app/System Entities/course';
import { AbstractControl } from '@angular/forms';
import { Instructor } from 'src/app/System Entities/Instructor';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  signInForm: FormGroup;
  AdminCreationForm: FormGroup<any>;
  AdminUploadInstructorsForm: FormGroup<any>;
  admin: Admin | null
  selectedSection: number
unregisteredInstructors: Instructor[] = [] 
courses: Course[] = []


  constructor(
    private formBuilder: NonNullableFormBuilder,
    private service:AdminService
  ) {

    this.signInForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.AdminCreationForm = this.formBuilder.group({
      CreatorAdminID: ['', Validators.required],
      NewAdminID: ['', Validators.required],
      NewAdminPassword: ['', Validators.required],
      NewAdminName: ['', Validators.required]
    });

    this.AdminUploadInstructorsForm = this.formBuilder.group({
      uploaded_file: ['', [Validators.required]]

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
  async CreateAdmin(){
    if(this.AdminCreationForm.valid){
      const id = this.AdminCreationForm.get('CreatorAdminID')?.value;
      const NewAdminID = this.AdminCreationForm.get('NewAdminID')?.value;
      const NewAdminPassword = this.AdminCreationForm.get('NewAdminPassword')?.value;
      const NewAdminName =this.AdminCreationForm.get('NewAdminName')?.value;
      console.log('Registering new Admin with ID: ', NewAdminID,' , password: ',NewAdminPassword,' and Name: ',NewAdminName,' Admin was registered by Admin # ',id);
 
      let admin = new Admin(
        this.AdminCreationForm.get('NewAdminID')?.value,
        this.AdminCreationForm.get('NewAdminPassword')?.value,
        this.AdminCreationForm.get('NewAdminName')?.value,
        this.AdminCreationForm.get('CreatorAdminID')?.value
      );
            let isSuccess: boolean | null = await this.service.createAdmin(admin)
    }
  }

  selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    if(this.selectedSection == 2){
      this.showUnregisteredInstructors();
    }
    if(this.selectedSection == 3){
      this.showCourses();
    }
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

  UploadInstructors(){
    if(this.AdminUploadInstructorsForm.valid && this.csvFileValidator()){
      const uploaded_file = this.AdminUploadInstructorsForm.get('uploaded_file');
      alert('Uploading Instructors from CSV file: ')
      console.log('Uploading Instructors from CSV file: ', uploaded_file);

      // Add the code here to send the CSV file to the backend




    }
    else{
      alert('Only CSV files are allowed');
    }
  }

  csvFileValidator() {
    const control = this.AdminUploadInstructorsForm.get('uploaded_file') || null;
    if(control){
      const file = control?.value;
      const extension = file.split('.').pop()
      const isCsv = extension === 'csv';
      return isCsv;
    }
    return false
  }

  async showCourses(){
    this.courses = await this.service.getAllCourses()
  }
  async showUnregisteredInstructors(){
    this.unregisteredInstructors = await this.service.getAllUnregisteredInstructors()
  }
}
