import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Show } from '../models/Show.model';
import { TmdbService } from '../services/tmdb.service';

@Component({
  selector: 'app-search-list',
  templateUrl: './search-list.component.html',
  styleUrls: ['./search-list.component.css']
})
export class SearchListComponent implements OnInit, OnDestroy {

  form!: FormGroup
  query!: string
  matchingShows: Show[] = []
  routeSub$!:Subscription

  constructor(
    private tmdbService: TmdbService,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.routeSub$ = this.activatedRoute.queryParams.subscribe((params) => {
      this.query = params['query']
      console.log(params['query'])
      this.getShowsBySearch()
    })
    this.form = this.createSearchForm()
  }

  ngOnDestroy(): void {
      this.routeSub$.unsubscribe()
  }

  getShowsBySearch() {
    this.tmdbService.searchShows(this.query).then((res) => {
      this.matchingShows = res
    })
    .catch((err) => {
      console.log(err)
    })
  }

  submitSearch(): void {
    const query = this.form.get('query')?.value
    console.log(query)
    this.router.navigate(['search'], {queryParams: { query: query }} )
  }

  createSearchForm(): FormGroup {
    return this.fb.group({
      query: this.fb.control('', [Validators.required])
    })
  }

  goToHome(): void {
    this.router.navigate([''])
  }
}
