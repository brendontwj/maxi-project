import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MaterialModule } from './material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { TrendingShowsComponent } from './components/trending-shows.component';
import { ShowDetailsComponent } from './components/show-details.component';
import { SearchListComponent } from './components/search-list.component';
import { LoginComponent } from './components/login.component';
import { RegisterComponent } from './components/register.component';
import { FavouritesComponent } from './components/favourites.component';


@NgModule({
  declarations: [
    AppComponent,
    TrendingShowsComponent,
    ShowDetailsComponent,
    SearchListComponent,
    LoginComponent,
    RegisterComponent,
    FavouritesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
