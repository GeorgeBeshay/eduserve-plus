import { Component } from '@angular/core';
import { Admin } from './admin';

@Component({
  selector: 'app-admin-sign-in',
  templateUrl: './admin-sign-in.component.html',
  styleUrls: ['./admin-sign-in.component.css']
})
export class AdminSignInComponent {
  form = new Admin('','')

}
