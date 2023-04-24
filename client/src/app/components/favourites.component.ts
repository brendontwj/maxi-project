import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Show } from '../models/Show.model';
import { UserService } from '../services/User.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent {
  form!: FormGroup
  username!: string | null
  favouriteShows: Show[] = []

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.form = this.createSearchForm()
      this.username = localStorage.getItem("username")
      this.getFavourites()
  }

  submitSearch(): void {
    const query = this.form.get('query')?.value
    console.log(query)
    this.router.navigate(['search'], {queryParams: { query: query }} )
  }

  getFavourites() {
    this.userService.getFavourites(this.username || "")
      .then((res) => {
        this.favouriteShows = res
        console.log(this.favouriteShows)
      }).catch((err) => {
        console.log(err)
      })
  }

  deleteShow(i: number) {
    const show = this.favouriteShows.at(i) as Show

    this.userService.deleteShow(show, this.username || "").then(result => {
      console.log(result['message'])
      this.getFavourites()
    }).catch(error => {
      console.log(error)
    })
  }

  createSearchForm(): FormGroup {
    return this.fb.group({
      query: this.fb.control('', [Validators.required])
    })
  }
}
