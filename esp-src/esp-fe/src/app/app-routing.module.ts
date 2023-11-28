import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {AboutComponent} from "./components/about/about.component";
import { AdminSignInComponent } from './components/admin-sign-in/admin-sign-in.component';
import { StudentSignUpComponent } from './components/student-sign-up/student-sign-up.component';
import {StudentComponent} from "./components/student/student.component";
import {AdminComponent} from "./components/admin/admin.component";

const routes: Routes = [
  {
    path: '', redirectTo: '/home', pathMatch: 'full'
  },
  {
    component: HomeComponent,
    path: 'home'
  },
  {
    component: AboutComponent,
    path: 'about'
  },
  {
    component: AdminSignInComponent,
    path: 'admin'
  },
  {
    component: StudentSignUpComponent,
    path: 'student-signup'
  },
  {

    component: StudentComponent,
    path: 'student'
  },
  {
    component: AdminComponent,
    path: 'admin'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponenets = [AdminSignInComponent,StudentSignUpComponent]
