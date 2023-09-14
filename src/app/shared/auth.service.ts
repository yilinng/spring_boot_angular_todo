import { Injectable } from '@angular/core';
import { UserEntry, Email, UserInformation } from '../types/types';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { MessageService } from './../message.service'

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  endpoint: string = 'http://localhost:8080/api/auth';
  headers = new HttpHeaders().set('Content-Type', 'application/json').set('Authorization', "Bearer " + this.getToken())
  currentUser = {};
  constructor(private http: HttpClient, public router: Router, private messageService: MessageService, private cookieService: CookieService) { }
  // Sign-up
  signUp(user: UserEntry): Observable<any> {
    let api = `${this.endpoint}/signup`;
    return this.http.post(api, user).pipe(
      tap((res: UserInformation) => this.log(`added user ${res}`)),
      //catchError(this.handleError<UserEntry>('signup'))
    );
  }
  // Sign-in
  logIn(user: UserEntry) {
    let api = `${this.endpoint}/login`;
    return this.http
      .post<any>(api, user).pipe(
        
        tap((res: any) => {
          this.cookieService.set('access_token', res.accessToken);
          localStorage.setItem('access_token', res.accessToken);
          this.getUser();
          this.log(`login user ${res}`)
        }),
      // catchError(this.handleError<UserEntry>('login'))
    );
  };

  getToken() {
    return this.cookieService.get('access_token');//localStorage.getItem('access_token');
  }

  get isLoggedIn(): boolean {
    const isLoggedIn = this.getToken() ? true : false
    //console.log('isLoggedIn', isLoggedIn)
    return isLoggedIn;
  }

  doLogout() {
    return this.http.post(`${this.endpoint}/logout`, { headers: this.headers }).pipe(
      tap((res: any) => {
        this.cookieService.delete('access_token');
        localStorage.removeItem('access_token');
        this.currentUser = {}
        this.log(`logout user ${res}`)
      }),
      //catchError(this.handleError<UserEntry>('logout'))
    );
  }

  private getUser() {
    const api = `${this.endpoint}/user`;
    return this.http.get(api, { headers: this.headers }).pipe(
      map((res) => {
        console.log('getUserProfile', res)
        this.currentUser = res;
        return res || {};
      }),
     // catchError(this.handleError<UserEntry>('getUserProfile'))
    );
  }
  // User profile
  getUserProfile(): Observable<any> {

    let pass_Id = this.cookieService.get('user_id');

    //https://www.freecodecamp.org/news/encode-decode-html-base64-using-javascript/
    //decode base64
    let deocdePass_Id = atob(pass_Id);

    console.log('deocdePass_Id', deocdePass_Id);

    
    const api = `${this.endpoint}/user`;
    return this.http.get(api, { headers: this.headers }).pipe(
      map((res) => {
        console.log('getUserProfile', res)
        this.currentUser = res;
        return res || {};
      }),
     // catchError(this.handleError<UserEntry>('getUserProfile'))
    );
  }

  //get email
  getEmail() {
    const url = `${this.endpoint}/email`;
    const emails = this.http.get<Email>(url);
    this.log(`authService: fetched email`)
    return emails
  }
  // Error
  /*
  handleError(error: HttpErrorResponse) {
    let msg = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      msg = error.error.message;
    } else {
      // server-side error
      msg = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(msg);
  }
  */
  /**
  * Handle Http operation that failed.
  * Let the app continue.
  *
  * @param operation - name of the operation that failed
  * @param result - optional value to return as the observable result
  */
  /*
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      // console.error(error); // log to console instead
      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
  */
  /** Log a authService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`authService: ${message}`);
  }
}