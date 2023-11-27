import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminSignInComponent } from './admin-sign-in/admin-sign-in.component';

const routes: Routes = [
  { path: 'admin-sign-in', component: AdminSignInComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponenets = [AdminSignInComponent]
