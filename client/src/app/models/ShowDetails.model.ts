import { Review } from "./Review.model"

export interface ShowDetails {
    id: number
    name: string
    poster_path: string
    media_type: string
    vote_average: number
    release_date: string
    overview: string
    creators: string[]
    genres: string[]
    reviews: Review[]
}