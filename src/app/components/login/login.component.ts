import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from './../../shared/auth.service';
import { Router } from '@angular/router';
import { validMail } from '../../shared/forbidden-email.drective'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  error: string = ''

  loginForm: FormGroup;
  constructor(
    public fb: FormBuilder,
    public authService: AuthService,
    public router: Router
  ) {

    this.loginForm = this.fb.group({
    //https://stackoverflow.com/questions/67883894/how-to-resolve-email-validation-error-in-reactive-forms-object-is-possibly-nul
    //https://angular.io/api/forms/Validators#maxlength
      usernameOrEmail: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(4)]],
    });

  }
  ngOnInit() {
  }

  
  /* 
  matchEmail() {
    if (validMail(this.loginForm.value.email)) {
     return true   
    }
    return false
  }
*/
  checkValue() {
    if (this.loginForm.value.usernameOrEmail && this.loginForm.value.password) {
      return false
    }
    return true
  }


  loginUser() {
    this.authService.logIn(this.loginForm.value).subscribe(
      res => {
      this.loginForm.reset();
      console.log('login success subscribe res', res);
      this.router.navigate(['user-profile']);
      }, (error) => {
        if (error.status === 400) {
          console.log(error.message)
          this.error = 'Invalid email or password'
          return
        }
        console.error(error);
        this.error = error.message
        //return error
      }
      )
   
  }

}
