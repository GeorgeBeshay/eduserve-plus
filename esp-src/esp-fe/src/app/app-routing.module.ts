import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {AboutComponent} from "./components/about/about.component";
import {StudentComponent} from "./components/student/student.component";

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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
