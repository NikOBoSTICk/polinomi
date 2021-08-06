import { Injectable } from '@angular/core';
import {HttpBackend, HttpClient, HttpHeaders} from "@angular/common/http";
import { Observable, of } from "rxjs";
import { Series } from "../models/series";
import { catchError } from "rxjs/operators";
import { MessageService } from "./message.service";

@Injectable({
  providedIn: 'root'
})
export class SeriesService {
  private seriesUrl = 'http://localhost:8000/series/search/';

  httpWithoutInterceptor: HttpClient;

  constructor(private http: HttpClient,
              private httpBackend: HttpBackend,
              private messageService: MessageService) {
    this.httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  getAllSeries(pageNumber:number, pageSize:number, sortBy:string): Observable<any> {
    return this.httpWithoutInterceptor.get<any>(
      this.seriesUrl + 'all' +  '?pageSize=' + pageSize + '&pageNumber=' + pageNumber + '&sortBy=' + sortBy)
      .pipe(
      catchError(this.handleError<any>('getAllSeries', []))
    );
  }

  getSeriesById(id:number): Observable<Series> {
    return this.httpWithoutInterceptor.get<Series>(
      this.seriesUrl + id)
      .pipe(
        catchError(this.handleError<Series>('getSeriesById'))
      );
  }

  deleteSeries(title:string): Observable<any>{
    return this.http.delete(this.seriesUrl, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {
        title: title
      },
    }).pipe(
      catchError(this.handleError<Series>('deleteSeries'))
    );
  }

  updateSeries(title:string, episodes:number, plot:string, genres:string[]): Observable<any>{
    return this.http.put(this.seriesUrl, {
      title: title,
      episodes: episodes,
      plot: plot,
      genres: genres
    }).pipe(
      catchError(this.handleError<Series>('updateSeries'))
    );
  }

  getSeriesByRating(rating:number): Observable<Series[]> {
    return this.httpWithoutInterceptor.get<Series[]>(
      this.seriesUrl + 'by_rating' + '?rating=' + rating)
      .pipe(
        catchError(this.handleError<Series[]>('getSeriesByRating', []))
      );
  }

  getSeriesByViews(min:number, max:number): Observable<Series[]> {
    return this.httpWithoutInterceptor.get<Series[]>(
      this.seriesUrl + 'by_views'+ '?minViews=' + min + '&maxViews=' + max)
      .pipe(
        catchError(this.handleError<Series[]>('getSeriesByViews', []))
      );
  }

  getSeriesByTitle(title:string): Observable<Series> {
    return this.httpWithoutInterceptor.get<Series>(
      this.seriesUrl + 'by_title' + '?title=' + title)
      .pipe(
        catchError(this.handleError<Series>('getSeriesByTitle'))
      );
  }

  getSeriesByGenre(genre:string): Observable<Series[]> {
    return this.httpWithoutInterceptor.get<Series[]>(
      this.seriesUrl + 'by_genre' + '?genre=' + genre)
      .pipe(
        catchError(this.handleError<Series[]>('getSeriesByGenre', []))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add(`SeriesService: ${message}`);
  }
}

