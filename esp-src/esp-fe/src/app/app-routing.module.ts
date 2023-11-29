import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {AboutComponent} from "./components/about/about.component";
import {StudentComponent} from "./components/student/student.component";
import {AdminComponent} from "./components/admin/admin.component";
import {InstructorComponent} from "./components/instructor/instructor.component";

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
    component: StudentComponent,
    path: 'student'
  },
  {
    component: AdminComponent,
    path: 'admin'
  },
  {
    component: InstructorComponent,
    path: 'instructor'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponenets = [AdminSignInComponent,StudentSignUpComponent]
