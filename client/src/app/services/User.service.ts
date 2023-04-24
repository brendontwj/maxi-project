import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { User } from '../models/User.model';
import { Show } from '../models/Show.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  user!: User
  redirectUrl!: string

  constructor(private httpClient: HttpClient) { }

  registerUser(user: User): Promise<any> {
    console.log("inside register user method of service")
    const fd: FormData = new FormData()

    fd.set("username", user.username)
    fd.set("password", user.password)
    fd.set("email", user.email)

    return firstValueFrom(
      this.httpClient.post<any>(
        'http://localhost:8080/api/register', fd
      )
    )
  }

  authUser(user: User): Promise<any> {
    console.log("inside auth user method of service")
    const fd: FormData = new FormData

    fd.set("username", user.username)
    fd.set("password", user.password)

    return firstValueFrom(
      this.httpClient.post<any>(
        'http://localhost:8080/api/login', fd
      )
    )
  }

  addToFavourites(show: Show, username: string) {
    const fd = new FormData

    fd.set('mediaId', show.id.toString())
    fd.set('name', show.name)
    fd.set('poster_path', show.poster_path)
    fd.set('mediaType', show.media_type)
    fd.set('vote_average', show.vote_average.toString())  
    fd.set('release_date', show.release_date)
    fd.set('username', username)

    return firstValueFrom(
      this.httpClient.post<any>(
        `http://localhost:8080/api/post/${username}/favourites`, fd
      )
    )
  }

  deleteShow(show: Show, username: string) {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {
        show: show
      }
    }

    // fd.set('mediaId', show.id.toString())
    // fd.set('name', show.name)
    // fd.set('poster_path', show.poster_path)
    // fd.set('mediaType', show.media_type)
    // fd.set('vote_average', show.vote_average.toString())  
    // fd.set('release_date', show.release_date)
    // fd.set('username', username)

    return firstValueFrom(
      this.httpClient.delete<any>(
        `http://localhost:8080/api/delete/${username}/favourites`, options
      )
    )
  }

  getFavourites(username: string) {
    return firstValueFrom(
      this.httpClient.get<Show[]>(
        `http://localhost:8080/api/${username}/favourites`,
      )
    )
  }
  
}
