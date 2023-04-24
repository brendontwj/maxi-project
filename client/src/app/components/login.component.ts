import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../models/User.model';
import { UserService } from '../services/User.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm!: FormGroup
  user!: User
  message!: string | null
  error!: string | null
  redirectUrl!: string | null

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.loginForm = this.createLoginForm()
      this.redirectUrl = this.userService.redirectUrl
  }

  createLoginForm() {
    return this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.minLength(3)]),
      password: this.fb.control('', [Validators.required, Validators.minLength(5)])
    })
  }

  loginUser() {
    this.user = this.loginForm.value as User

    this.userService.authUser(this.user).then(result => {
    
      localStorage.setItem("username", result['username'])
      localStorage.setItem("email", result['email'])

      console.log(this.redirectUrl);
      
      if (this.redirectUrl != null) {
        console.log("inside redirect fragment");
        this.router.navigateByUrl(this.redirectUrl)
        this.userService.redirectUrl = ""
      } else {
        this.router.navigate([''])
      }
    }).catch (error => {
      console.log(error)
      this.message = error.error.message
      this.error = error.error.error
    })
  }
}
