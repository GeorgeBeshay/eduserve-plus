import { Injectable } from '@angular/core';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InstructorService {

  signInurl = 'http://localhost:8081/esp-server/instructor-controller/signIn'
  signUpurl = 'http://localhost:8081/esp-server/instructor-controller/signUp'

  constructor() { }
}

export class Instructor {

  instructorId: string
  instructorPwHash: string
  departmentId: string
  instructorName: string
  phone: string
  email: string
  officeHours: string

  constructor(instructorId: string, instructorPwHash: string, departmentId: string, instructorName: string,
    phone: string, email: string, officeHours: string) {
      this.instructorId = instructorId
      this.instructorPwHash = instructorPwHash
      this.departmentId = departmentId
      this.instructorName = instructorName
      this.phone = phone
      this.email = email
      this.officeHours = officeHours
    }
}
