export class Course {
    courseCode: string
    courseName: string
    courseDescription: string
    offeringDpt: number
    creditHrs: number
    prerequisite: string[]

    constructor(courseCode: string, courseName: string, courseDescription: string,
                offeringDpt: number, creditHrs: number, prerequisite: string[]) {
        this.courseCode = courseCode
        this.courseName = courseName
        this.courseDescription = courseDescription 
        this.offeringDpt = offeringDpt
        this.creditHrs = creditHrs
        this.prerequisite = prerequisite
    }
}
