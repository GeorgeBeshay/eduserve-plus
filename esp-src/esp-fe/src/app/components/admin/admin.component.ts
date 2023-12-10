import {Component, OnInit} from '@angular/core';
import {FormGroup, NonNullableFormBuilder, Validators} from "@angular/forms";
import {AdminService} from '../../services/admin.service';
import {Admin} from "../../System Entities/Admin";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit{
  signInForm: FormGroup;
  AdminCreationForm: FormGroup<any>;
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
    this.AdminCreationForm = this.formBuilder.group({
      CreatorAdminID: ['', Validators.required],
      NewAdminID: ['', Validators.required],
      NewAdminPassword: ['', Validators.required],
      NewAdminName: ['', Validators.required]
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
  async CreateAdmin(){
    if(this.AdminCreationForm.valid){
      const id = this.AdminCreationForm.get('CreatorAdminID')?.value;
      const NewAdminID = this.AdminCreationForm.get('NewAdminID')?.value;
      const NewAdminPassword = this.AdminCreationForm.get('NewAdminPassword')?.value;
      const NewAdminName =this.AdminCreationForm.get('NewAdminName')?.value;
      console.log('Registering new Admin with ID: ', NewAdminID,' , password: ',NewAdminPassword,' and Name: ',NewAdminName,' Admin was registered by Admin # ',id);
 
      let admin = new Admin(
        this.AdminCreationForm.get('NewAdminID')?.value,
        this.AdminCreationForm.get('NewAdminPassword')?.value,
        this.AdminCreationForm.get('NewAdminName')?.value,
        this.AdminCreationForm.get('CreatorAdminID')?.value
      );
            let isSuccess: boolean | null = await this.service.createAdmin(admin)
    }
  }

  selectSection (sectionIndex: number) {
    this.selectedSection = sectionIndex
    console.log(this.selectedSection)
  }

}
