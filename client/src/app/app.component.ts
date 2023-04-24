import { Component } from '@angular/core';
import { Event, NavigationEnd, NavigationStart, Router } from '@angular/router';
import { UserService } from './services/User.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  isLoggedIn = false
  username!: string | null
  color = ""

  constructor(
      private router: Router,
      private userService: UserService
    ) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        if (localStorage.getItem('username') == null) {
          this.isLoggedIn = false
        } else {
          this.username = localStorage.getItem('username')
          this.isLoggedIn = true
        }
      }
  });
  }

  login() {
    this.userService.redirectUrl = this.router.url
    this.router.navigate(['/login'])
  }
  
  logout() {
    localStorage.removeItem('username')
    localStorage.removeItem('email')
    this.router.navigate(['dsadsadsad'])
  }

}
