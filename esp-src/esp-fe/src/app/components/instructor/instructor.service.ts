import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { InstructorComponent } from './instructor.component'
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class InstructorService {
  url = 'http://localhost:3000/SignUp'

  constructor(private http: HttpClient) { }

   SignUp(instructor: InstructorComponent){
     return this.http.post<any>(this.url,instructor)
     .pipe(catchError(this.errorHandler))
  }
  errorHandler(error: HttpErrorResponse){
      return  throwError(error)
  }
}
