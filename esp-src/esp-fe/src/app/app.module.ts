import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule , routingComponenets} from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminSignInComponent } from './admin-sign-in/admin-sign-in.component';

@NgModule({
  declarations: [
    AppComponent,
    routingComponenets
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
