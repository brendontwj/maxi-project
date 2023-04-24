import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Show } from '../models/Show.model';
import { TmdbService } from '../services/tmdb.service';
import { UserService } from '../services/User.service';

@Component({
  selector: 'app-trending-shows',
  templateUrl: './trending-shows.component.html',
  styleUrls: ['./trending-shows.component.css']
})
export class TrendingShowsComponent {

  form!: FormGroup
  trendingShows: Show[] = []
  url!: string

  constructor(
    private tmdbService: TmdbService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.form = this.createSearchForm()
      this.getTrendingShows()
  }

  submitSearch(): void {
    const query = this.form.get('query')?.value
    console.log(query)
    this.router.navigate(['search'], {queryParams: { query: query }} )
  }

  getTrendingShows() {
    this.tmdbService.getTrending()
      .then((res) => {
        this.trendingShows = res
        console.log(this.trendingShows)
      }).catch((err) => {
        console.log(err)
      })
  }

  createSearchForm(): FormGroup {
    return this.fb.group({
      query: this.fb.control('', [Validators.required])
    })
  }
}
