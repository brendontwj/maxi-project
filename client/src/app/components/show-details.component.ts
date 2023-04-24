import { Component, OnDestroy, OnInit,  } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router,  } from '@angular/router';
import { Subscription } from 'rxjs';
import { ShowDetails } from '../models/ShowDetails.model';
import { Review } from '../models/Review.model'
import { TmdbService } from '../services/tmdb.service';
import { UserService } from '../services/User.service';
import { Show } from '../models/Show.model';

@Component({
  selector: 'app-show-details',
  templateUrl: './show-details.component.html',
  styleUrls: ['./show-details.component.css']
})
export class ShowDetailsComponent implements OnInit, OnDestroy {

  media_type!: string
  id!: number
  show!: ShowDetails
  routeSub$!: Subscription
  reviewForm!: FormGroup
  review!: Review
  message!: string | null
  username!: string | null
  showToFavourite: Show = {
    id: 0,
    name: "",
    poster_path: "",
    media_type: "",
    vote_average: 0,
    release_date: "",
  }
  isLoaded = false

  constructor(
    private activatedRoute: ActivatedRoute,
    private tmdbService: TmdbService,
    private router: Router,
    private fb: FormBuilder,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
      this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
        this.media_type = params['media_type']
        this.id = params['id']
      })
      this.getShow()
      this.getReviews()
      this.username = localStorage.getItem("username")
      this.reviewForm = this.createReviewForm()
  }

  ngOnDestroy(): void {
      this.routeSub$.unsubscribe()
  }

  getShow() {
    this.tmdbService.getShowByTypeAndId(this.media_type, this.id).then((res) => {
      this.show = res
    }).catch((err) => {
      console.log(err)
      this.message = err.error.message
    })   
  }

  getReviews() {
    this.tmdbService.getReviews(this.media_type, this.id).then(result => {
      console.log(result);
      this.show.reviews = result as Review[]
    }).catch(err => {
      console.log(err);
    })
  }

  createReviewForm(): FormGroup {
    return this.fb.group({
      rating: this.fb.control('5', [Validators.required]),
      comment: this.fb.control('', [Validators.required, Validators.minLength(30)])
    })
  }

  submitReview(): void {
    console.log('inside submit review')
    console.log('username is: ' + localStorage.getItem('username'));
    
    if (localStorage.getItem('username') == null) {
      this.userService.redirectUrl = this.router.url
      console.log(this.userService.redirectUrl);
      this.router.navigate(['login'])
    } else {
      this.review = this.reviewForm.value as Review
      this.review.username = this.username || ""
      this.review.mediaId = this.id.toString()
      this.review.mediaType = this.media_type
      console.log(this.review.toString());
      this.tmdbService.postReview(this.review).then(result => {
        console.log(result['message'])
        this.getReviews()
      }).catch(err => {
        console.log(err);
        this.message = err.error.message
      })
      
    }
  }

  addToFavourites() {
    console.log(this.id);
    
    this.showToFavourite.id = this.id
    this.showToFavourite.name = this.show.name
    this.showToFavourite.media_type = this.show.media_type
    this.showToFavourite.poster_path = this.show.poster_path
    this.showToFavourite.release_date = this.show.release_date
    this.showToFavourite.vote_average = this.show.vote_average

    this.userService.addToFavourites(this.showToFavourite, this.username || "").then(result => {
      console.log(result);
    }).catch(err => {
      console.log(err);
    })
  }

  deleteReview(i: number): void {
    console.log("inside delete review");
    const review = this.show.reviews.at(i) as Review

    this.tmdbService.deleteReview(review).then(result => {
      console.log(result['message'])
      this.getReviews()
    }).catch(error => {
      console.log(error);
    })
  }

  goToHome(): void {
    this.router.navigate([''])
  }
}
