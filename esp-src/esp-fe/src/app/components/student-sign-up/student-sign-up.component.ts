import { Component } from '@angular/core';
import { Student } from './student';
import { StudentService } from './student.service';

@Component({
  selector: 'app-student-sign-up',
  templateUrl: './student-sign-up.component.html',
  styleUrls: ['./student-sign-up.component.css']
})
export class StudentSignUpComponent {
  constructor(private studentService: StudentService){}
  form = new Student('','','','','','','','','','','')
  onSubmit() {
    console.log(this.form)
    this.studentService.signUp(this.form)
    .subscribe(
      data => console.log('success', data),
      error => console.log('error',error)
    )
  }

}
