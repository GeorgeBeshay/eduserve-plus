export class Student{

  studentId: string
  studentPwHash: string
  departmentId: string
  studentLevel: string
  gpa: string
  studentName: string
  ssn: string
  birthDate: string
  studentAddress: string
  phone: string
  landline: string
  gender: string
  email: string

  constructor(studentId: string, studentPwHash: string, departmentId: string, studentLevel: string, gpa: string,
              studentName: string, ssn: string, birthDate: string, studentAddress: string, phone: string,
              landline: string, gender: string, email: string){

    this.studentId = studentId
    this.studentPwHash = studentPwHash
    this.departmentId = departmentId
    this.studentLevel = studentLevel
    this.gpa = gpa
    this.studentName = studentName
    this.ssn = ssn
    this.birthDate = birthDate
    this.studentAddress = studentAddress
    this.phone = phone
    this.landline = landline
    this.gender = gender
    this.email = email

  }
}
