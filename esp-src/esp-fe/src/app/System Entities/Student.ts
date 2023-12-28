export class Student{

  studentId: string
  dptId: string
  studentLevel: string
  gpa: string
  studentName: string
  ssn: string
  bdate: string
  studentAddress: string
  phone: string
  landline: string
  gender: string
  email: string

  constructor(studentId: string, dptId: string, studentLevel: string, gpa: string,
              studentName: string, ssn: string, bdate: string, studentAddress: string, phone: string,
              landline: string, gender: string, email: string){

    this.studentId = studentId
    this.dptId = dptId
    this.studentLevel = studentLevel
    this.gpa = gpa
    this.studentName = studentName
    this.ssn = ssn
    this.bdate = bdate
    this.studentAddress = studentAddress
    this.phone = phone
    this.landline = landline
    this.gender = gender
    this.email = email

  }
}
