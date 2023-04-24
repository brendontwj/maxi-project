import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { Show } from '../models/Show.model';
import { ShowDetails } from '../models/ShowDetails.model';
import { Review } from '../models/Review.model';

@Injectable({
  providedIn: 'root'
})
export class TmdbService {

  constructor(private httpClient: HttpClient) { }

  getTrending() {
    return lastValueFrom(
      this.httpClient.get<Show[]>(
        'http://localhost:8080/api/trending'
      )
    )
  }

  getShowByTypeAndId(media_type: string, id: number) {
    return lastValueFrom(
      this.httpClient.get<ShowDetails>(
        `http://localhost:8080/api/${media_type}/${id}`
      )
    )
  }

  searchShows(query: string) {
    const params = new HttpParams()
      .set(`query`, query)

    return lastValueFrom(
      this.httpClient.get<Show[]>(
        `http://localhost:8080/api/search`,
        {
          params: params
        }
      )
    )
  }

  postReview(review: Review) {
    const fd = new FormData()

    fd.set('username', review.username)
    fd.set('rating', review.rating.toString())
    fd.set('comment', review.comment)
    fd.set('mediaType', review.mediaType)
    fd.set('mediaId', review.mediaId)

    return lastValueFrom(
      this.httpClient.post<any>(
        'http://localhost:8080/api/post/review', fd
      )
    )
  }

  getReviews(mediaType: string, mediaId: number) {
    return lastValueFrom(
      this.httpClient.get<Review[]>(
        `http://localhost:8080/api/${mediaType}/${mediaId}/reviews`
      )
    )
  }

  deleteReview(review: Review) {
    const fd = new FormData()

    fd.set('username', review.username)
    fd.set('rating', review.rating.toString())
    fd.set('comment', review.comment)
    fd.set('mediaType', review.mediaType)
    fd.set('mediaId', review.mediaId)

    return lastValueFrom(
      this.httpClient.post<any>(
        `http://localhost:8080/api/${review.mediaType}/${review.mediaId}/reviews`, fd
      )
    )
  }
}
