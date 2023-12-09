import {Component, OnInit} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, Validators} from "@angular/forms";
import {AdminService} from 'src/app/services/admin.service';
import {Admin} from "../../System Entities/Admin";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  signInForm: FormGroup;
  admin: Admin | null
  selectedSection: number

  constructor(
    private formBuilder: NonNullableFormBuilder,
    private service:AdminService
  ) {

    this.signInForm = this.formBuilder.group({
      id: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.admin = null
    this.selectedSection = 0

  }

  ngOnInit() {
    // Any initialization logic
  }

  async onSubmit() {
    if (this.signInForm.valid) {
      const id = this.signInForm.value.id;
      const password = this.signInForm.value.password;

      // Placeholder: Simulate authentication logic
      console.log('Signing in with ID:', id, 'and password:', password);

      let isSuccess: boolean | null = await this.service.signIn(id, password);

      if (isSuccess) {
        alert("The admin has been signIn successfully")
        this.admin = new Admin("", "", "", "")
      } else {
        alert("The ID or the password is not correct")
      }

    }
  }

  selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
  }

}
