import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StudentService} from 'src/app/services/student.service';
import {Student} from "../../System Entities/Student";
import Swal from "sweetalert2";
import {Course} from "../../System Entities/Course";

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

  constructor(private formBuilder: FormBuilder, private studentService:StudentService) {
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

    if(this.selectedSection == 1) {
      this.loadCourses()
    }
  }

  async onSubmit() {
    if (this.signInForm.valid) {
      const id = this.signInForm.value.id;
      const password = this.signInForm.value.password;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, 'and password:', password);

      let isSuccess: boolean | null = await this.studentService.signIn(id, password);

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
        let isSuccess: boolean | null = await this.studentService.signUp(student, password, newPassword);

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

  // Course Enrollment Related Methods
  courses: Course[] = [
    { courseCode: 'ABC123', courseName: 'Course 1', creditHrs: 3, courseDescription: "This is the first course description", offeringDpt: 1, prerequisite: ["Pre req1"] },
    { courseCode: 'DEF456', courseName: 'Course 2', creditHrs: 4, courseDescription: "This is the first course description", offeringDpt: 1, prerequisite: ["Pre req1"] },
    { courseCode: 'DEF452', courseName: 'Course 3', creditHrs: 4, courseDescription: "This is the first course description", offeringDpt: 1, prerequisite: ["Pre req1"] },
    { courseCode: 'DEF453', courseName: 'Course 4', creditHrs: 4, courseDescription: "This is the first course description", offeringDpt: 1, prerequisite: ["Pre req1"] },
    { courseCode: 'DEF454', courseName: 'Course 5', creditHrs: 4, courseDescription: "This is the first course description", offeringDpt: 1, prerequisite: ["Pre req1"] },
    { courseCode: 'DEF455', courseName: 'Course 6', creditHrs: 4, courseDescription: "This is the first course description", offeringDpt: 1, prerequisite: ["Pre req1"] },

  ];  // TODO: let courses = [] when implementing the endpoint ..

  enrolledCourses: Course[] = []; // Initially enrolled courses

  totalAvailableCreditHours: number = 21; // Example total available credit hours

  isEnrolled(course: Course): boolean {
    return this.enrolledCourses.some(c => c.courseCode === course.courseCode);
  }

  async enroll(course: Course): Promise<void> {

    if (course.creditHrs > this.totalAvailableCreditHours) {
      await Swal.fire({
        icon: "error",
        title: "Oops...",
        text: "You are out of Credit Hours ..",
      });
      return;
    }

    this.totalAvailableCreditHours -= course.creditHrs;
    this.enrolledCourses.push(course);
  }

  unEnroll(course: Course): void {
    this.enrolledCourses = this.enrolledCourses.filter(c => c.courseCode !== course.courseCode);
    this.totalAvailableCreditHours += course.creditHrs;
  }

  truncateDescription(description: string, limit: number): string {
    if (description.length > limit) {
      return description.substring(0, limit) + '...';
    }
    return description;
  }

  computeSelectedCoursesHrs() {
    let ans = 0;
    for (const course of this.enrolledCourses) {
      ans += course.creditHrs;
    }
    return ans;
  }

  /**
   * Functions calls the suitable method from the service object, to register the selected courses.
   */
  registerCourses() {
    // TODO: Call the appropriate end point method from the studentService to register the selected courses.
    let studentId = this.student?.studentId;
    let selectedCourses = this.enrolledCourses  // list of objects containing all the course data.
    let totalRegisteredHours = this.computeSelectedCoursesHrs();
    // this.studentService.registerSelectedCourses(bla bla..)
    // use sweet alert function (Swal.fire()) to display the alerts in a pretty form !

    // TODO: Remove those statements
    console.log(this.enrolledCourses);
    console.log(totalRegisteredHours);
  }

  /**
   * Function must be called upon navigating to the course enrollment page to fetch the suitable courses.
   */
  loadCourses() {
    let studentId = this.student?.studentId;
    // TODO: Call the appropriate end point method from the studentService.
    // let tempObj = this.studentService.loadAvailableCoursesForRegistration(this.student?.studentId);
    // this.courses = extract courses from tempObj

    // in case of varying # of hours uncomment the following:
    // this.totalAvailableCreditHours = extract # of hours from tempObj
  }


}
