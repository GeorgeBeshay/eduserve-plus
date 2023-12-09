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

