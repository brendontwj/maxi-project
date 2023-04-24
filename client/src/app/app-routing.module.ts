import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchListComponent } from './components/search-list.component';
import { ShowDetailsComponent } from './components/show-details.component';
import { TrendingShowsComponent } from './components/trending-shows.component';
import { LoginComponent } from './components/login.component';
import { RegisterComponent } from './components/register.component';
import { FavouritesComponent } from './components/favourites.component';

const routes: Routes = [
  { path: '', component: TrendingShowsComponent },
  { path: ':username/favourites', component: FavouritesComponent },
  { path: ':media_type/:id', component: ShowDetailsComponent },
  { path: 'search', component: SearchListComponent },
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
