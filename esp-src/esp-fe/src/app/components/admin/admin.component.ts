import {Component, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, NonNullableFormBuilder, Validators} from "@angular/forms";
import {AdminService} from '../../services/admin.service';
import {Admin} from "../../System Entities/Admin";
import { Course } from 'src/app/System Entities/Course';
import { AbstractControl } from '@angular/forms';
import { Instructor } from 'src/app/System Entities/Instructor';
import { Student } from 'src/app/System Entities/Student';
import Swal from 'sweetalert2';


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
  unregisteredInstructors: Instructor[] = [] 
  courses: Course[] = []
  unregisteredStudents: Student[] = []
  unregisteredInstructorfile : File | null = null;
  unregisteredStudentfile : File | null = null;
  unregisteredInstructorFile : File | null = null;
  unregisteredStudentFile : File | null = null;
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
      offeringDpt: ['', Validators.required],
      creditHrs: [null, [Validators.required, Validators.min(1), Validators.max(20)]],
      prerequisite: this.formBuilder.array([])
    });

    this.admin = null
    this.selectedSection = 0

  }

  ngOnInit() {
    // Restoring cached object.
    let tempObj = sessionStorage.getItem("adminObject");
    if(tempObj != null)
      this.admin = JSON.parse(tempObj);
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
          text: "Admin Sign In Process is Successful.",
          showConfirmButton: false,
          timer: 2000
        });

        this.admin = new Admin(id, password, "", "")

        // caching object.
        sessionStorage.setItem("adminObject", JSON.stringify(this.admin));

      } else {

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "The Given ID or Password is Incorrect.",
        });

      }

    }
  }
  async CreateAdmin(){
    if(this.AdminCreationForm.valid){

      const id = this.AdminCreationForm.get('CreatorAdminID')?.value;
      const NewAdminID = this.AdminCreationForm.get('NewAdminID')?.value;
      const NewAdminPassword = this.AdminCreationForm.get('NewAdminPassword')?.value;
      const NewAdminName = this.AdminCreationForm.get('NewAdminName')?.value;
      console.log('Registering new Admin with ID: ', NewAdminID,' , password: ',NewAdminPassword,' and Name: ',NewAdminName,' Admin was registered by Admin # ',id);

      let admin = new Admin(
        this.AdminCreationForm.get('NewAdminID')?.value,
        "0",
        this.AdminCreationForm.get('NewAdminName')?.value,
        this.AdminCreationForm.get('CreatorAdminID')?.value
      );

      let isSuccess: boolean | null = await this.service.createAdmin(admin, this.AdminCreationForm.get('NewAdminPassword')?.value)
      if(isSuccess) {

        await Swal.fire({
          position: "center",
          icon: "success",
          title: "Successful",
          text: "Admin Creation Process is Successful.",
          showConfirmButton: false,
          timer: 2000
        });

      } else {

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Failed to create an Admin.",
        });

      }
    }
  }

  selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    if(this.selectedSection == 5){
      this.showUnregisteredInstructors();
    }
    if(this.selectedSection == 6){
      this.showUnregisteredStudents();
    }
    if(this.selectedSection == 7){
      this.showCourses();
    }
    
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

        await Swal.fire({
          position: "center",
          icon: "success",
          title: "Successful",
          text: "Course Addition Process is Successful.",
          showConfirmButton: false,
          timer: 1500
        });

      }
      else{

        await Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Course Addition Process Failed.",
        });

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
      this.unregisteredInstructorFile = event.target.files[0];
    }
  }

  async uploadInstructors(){

    if(this.adminUploadInstructorsForm.valid
      && this.csvFileValidator(this.adminUploadInstructorsForm)){

      let uploaded_file = this.unregisteredInstructorFile;
      console.log(uploaded_file)

      let timerInterval: any;
      await Swal.fire({
        title: "Loading",
        html: `Uploading Instructors CSV File ... <br>Estimated remaining time: <b></b> ms.`,
        timer: 3500,
        timerProgressBar: true,
        didOpen: () => {
          Swal.showLoading();
          // @ts-ignore
          const timer = Swal.getPopup().querySelector("b");
          timerInterval = setInterval(() => {
            // @ts-ignore
            timer.textContent = `${Swal.getTimerLeft()}`;
          }, 100);
        },
        willClose: () => {
          clearInterval(timerInterval);
        }
      }).then((result) => {
        /* Read more about handling dismissals below */
        if (result.dismiss === Swal.DismissReason.timer) {
          console.log("Timer - SweetAlert Closed");
        }
      });

      if(uploaded_file){

        let results = await this.service.uploadUnregisteredInstructors(uploaded_file)

        if(results.instructorsAdded > 0){

          await Swal.fire({
            position: "center",
            icon: "success",
            title: "Successful",
            text: `Added ${results.instructorsAdded} Instructors Successfully`,
            showConfirmButton: false,
            timer: 2000
          });

          let failedRecords = "";
          for (const fail of results.instructorsNotAdded) {
            failedRecords += `Failed to add instructor in row ${fail}.\n`;
          }

          if (failedRecords != "") {
            await Swal.fire({
              icon: "error",
              title: "Oops...",
              text: failedRecords,
              timer: 4000
            });
          }

        } else {

          await Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "No instructors were added.",
            timer: 4000
          });

        }
      }
    } else {

      await Swal.fire({
        icon: "error",
        title: "Oops...",
        text: "Only CSV Files are Allowed !",
        timer: 1500
      });

    }
  }

  onStudentFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.unregisteredStudentFile = event.target.files[0];
    }
  }

  async uploadStudents() {

    if(this.adminUploadStudentsForm
      && this.csvFileValidator(this.adminUploadStudentsForm)) {

        let uploaded_file = this.unregisteredStudentFile;
        console.log('Uploading Students from CSV file: ', uploaded_file);

      let timerInterval: any;
      await Swal.fire({
        title: "Loading",
        html: `Uploading Students CSV File ... <br>Estimated remaining time: <b></b> ms.`,
        timer: 3500,
        timerProgressBar: true,
        didOpen: () => {
          Swal.showLoading();
          // @ts-ignore
          const timer = Swal.getPopup().querySelector("b");
          timerInterval = setInterval(() => {
            // @ts-ignore
            timer.textContent = `${Swal.getTimerLeft()}`;
          }, 100);
        },
        willClose: () => {
          clearInterval(timerInterval);
        }
      }).then((result) => {
        /* Read more about handling dismissals below */
        if (result.dismiss === Swal.DismissReason.timer) {
          console.log("Timer - SweetAlert Closed");
        }
      });

        if (uploaded_file) {

          let results = await this.service.uploadUnregisteredStudents(uploaded_file)

          if(results.studentsSuccessfullyAdded > 0){

            await Swal.fire({
              position: "center",
              icon: "success",
              title: "Successful",
              text: `Added ${results.studentsSuccessfullyAdded} Students Successfully.`,
              showConfirmButton: false,
              timer: 2000
            });

            let failedRecords = "";
            for (const fail of results.failedStudentsToBeAdded) {
              failedRecords += `Failed To Add Student in Row ${fail}.\n`;
            }

            if (failedRecords != "") {
              await Swal.fire({
                icon: "error",
                title: "Oops...",
                text: failedRecords,
                timer: 4000
              });
            }

          } else{

            await Swal.fire({
              icon: "error",
              title: "Oops...",
              text: "No Students Were Added.",
              timer: 1500
            });

          }
        }

    } else {

      await Swal.fire({
        icon: "error",
        title: "Oops...",
        text: "Only CSV Files are Allowed !",
      });

    }
  }

  csvFileValidator(csvForm: FormGroup) {
    const control = csvForm.get('uploaded_file') || null;
    if(control){
      const file = control?.value;
      const extension = file.split('.').pop()
      return extension === 'csv';
    }
    return false
  }

  async showCourses(){
    this.courses = await this.service.getAllCourses()
  }

  async showUnregisteredInstructors(){
    this.unregisteredInstructors = await this.service.getAllUnregisteredInstructors()
  }

  async showUnregisteredStudents(){
    this.unregisteredStudents = await this.service.getAllUnregisteredStudents()
  }
}
