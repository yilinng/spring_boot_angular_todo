import { Component } from '@angular/core';
//import { AuthService } from './shared/auth.service';
//import { Router } from '@angular/router';
import { environment } from './../environments/environment';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    // animation triggers go here
  ]
})
  
//https://stackoverflow.com/questions/47900447/how-to-change-page-title-with-routing-in-angular-application  
export class AppComponent {
  constructor(private titleService: Title) {
    this.titleService.setTitle('Home page')
    console.log(environment.production); // Logs false for development environment
   }
}