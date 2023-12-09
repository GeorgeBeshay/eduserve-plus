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
