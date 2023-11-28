import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {AboutComponent} from "./components/about/about.component";
import { AdminSignInComponent } from './components/admin-sign-in/admin-sign-in.component';
import { StudentSignUpComponent } from './components/student-sign-up/student-sign-up.component';

const routes: Routes = [
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
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponenets = [AdminSignInComponent,StudentSignUpComponent]
