import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../models/User.model';
import { UserService } from '../services/User.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup
  user!: User
  message!: string | null
  error!: string | null

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.registerForm = this.createRegisterForm()
  }

  createRegisterForm() {
    return this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.minLength(3)]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(5)])
    })
  }

  registerUser() {
    console.log("inside register user method of component")
    console.log(this.registerForm)
    this.user = this.registerForm.value as User

    this.userService.registerUser(this.user).then(result => {
      console.log(result)
      this.router.navigate(['login'])
    }).catch (error => {
      console.log(error)
      this.message = error.error.message
      this.error = error.error.error
    })
  }
}
