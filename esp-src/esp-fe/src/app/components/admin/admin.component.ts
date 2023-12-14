import {Component, OnInit} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, Validators, FormArray, FormControl} from "@angular/forms";
import {AdminService} from '../../services/admin.service';
import {Admin} from "../../System Entities/Admin";
import { Course } from 'src/app/System Entities/course';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  signInForm: FormGroup;
  AdminCreationForm: FormGroup<any>;
  adminUploadInstructorsForm: FormGroup<any>;
  adminUploadStudentsForm: FormGroup<any>;
  admin: Admin | null
  selectedSection: number
  unregisteredInstructorfile : File | null = null;
  unregisteredStudentfile : File | null = null;
  courseForm: FormGroup;


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

    this.adminUploadInstructorsForm = this.formBuilder.group({
      uploaded_file: ['', [Validators.required]]

    });

    this.adminUploadStudentsForm = this.formBuilder.group({
      uploaded_file: ['', [Validators.required]]

    });

    this.courseForm = this.formBuilder.group({
      courseCode: ['', [Validators.required, Validators.maxLength(7)]],
      courseName: ['', [Validators.required, Validators.maxLength(40)]],
      courseDescription: ['', Validators.required],
      offeringDpt: [null, [Validators.required, Validators.min(-128), Validators.max(127)]],
      creditHrs: [null, [Validators.required, Validators.min(-128), Validators.max(127)]],
      prerequisite: this.formBuilder.array([])
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
    console.log(this.selectedSection)
  }

  async addCourse () {

    if(this.courseForm.valid) {

      const courseCode = this.courseForm.get('courseCode')?.value;
      const courseName = this.courseForm.get('courseName')?.value;
      const courseDescription = this.courseForm.get('courseDescription')?.value
      const offeringDpt = this.courseForm.get('offeringDpt')?.value;
      const creditHrs = this.courseForm.get('creditHrs')?.value;
      const prerequisite = this.courseForm.get('prerequisite')?.value;

      console.log(courseCode)
      console.log(courseName)
      console.log(courseDescription)
      console.log(offeringDpt)
      console.log(creditHrs)
      console.log(prerequisite)

      let newCourse = new Course(courseCode, courseName, courseDescription, offeringDpt, creditHrs, prerequisite)

      let isSuccess: boolean | null = await this.service.addCourse(newCourse)

      if(isSuccess){
        alert("The course has been added successfully")
      }
      else{
        alert("The course has not been added successfully")
      }
    }
  }


  get prerequisite() {
    return this.courseForm.get('prerequisite') as FormArray;
  }

  addPrerequisite() {
    this.prerequisite.push(this.formBuilder.control('', [Validators.required, Validators.maxLength(7)]));
  }

  removePrerequisite(index: number) {
    this.prerequisite.removeAt(index);
  }

  get prerequisiteControls() {
    return (this.courseForm.get('prerequisite') as FormArray).controls as FormControl[];
  }


  onInstructorFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.unregisteredInstructorfile = event.target.files[0];
    }
  }

  async uploadInstructors(){

    if(this.adminUploadInstructorsForm.valid
      && this.csvFileValidator(this.adminUploadInstructorsForm)){

      let uploaded_file = this.unregisteredInstructorfile;
      console.log(uploaded_file)
      alert('Uploading Instructors from CSV file please wait a moment')

      if(uploaded_file){

        let InstructorsAdded: number = 0
        // Add the code here to send the CSV file to the backend

        // the function uploadUnregisteredInstructors need to be implemented as the uploadUnregisteredstudents
        // in the admin service

        // InstructorsAdded = await this.service.uploadUnregisteredInstructors(uploaded_file)

        if(InstructorsAdded > 0){
          alert(InstructorsAdded + ' Added Successfully')
        }
        else{
          alert('Error: No students were added')
        }
      }
    }
    else{
      alert('Only CSV files are allowed');
    }
  }

  onStudentFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.unregisteredStudentfile = event.target.files[0];
    }
  }

  async uploadStudents() {

    if(this.adminUploadStudentsForm
      && this.csvFileValidator(this.adminUploadStudentsForm)) {

        let uploaded_file = this.unregisteredStudentfile;
        console.log('Uploading Students from CSV file: ', uploaded_file);
        alert('Uploading Students from CSV file, please wait.')

        if (uploaded_file) {

          let results = await this.service.uploadUnregisteredStudents(uploaded_file)

          if(results.studentsSuccessfullyAdded > 0){
            alert(results.studentsSuccessfullyAdded + ' Added Successfully')
            for (var fail of results.failedStudentsToBeAdded) {
              alert('Student in row ' + fail + ' failed to be added.')
            }
          }
          else{
            alert('Error: No students were added')
          }
        }

    } else {
      alert('Only CSV files are allowed')
    }
  }

  csvFileValidator(csvForm: FormGroup) {
    const control = csvForm.get('uploaded_file') || null;
    if(control){
      const file = control?.value;
      const extension = file.split('.').pop()
      const isCsv = extension === 'csv';
      return isCsv;
    }
    return false
  }

}
