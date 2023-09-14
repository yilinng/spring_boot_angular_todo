import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn } from '@angular/forms';

@Directive({
  selector: '[appValidEmail]',
  providers: [{provide: NG_VALIDATORS, useExisting: EmailValidatorDirective, multi: true}]
})

  
  
/** A user's email can't match the given regular expression */
export class EmailValidatorDirective implements Validator {
  @Input('appValidEmail') appValidEmail = '';
  
  validate(control: AbstractControl<any, any>): ValidationErrors | null {
   return this.appValidEmail ? EmaileValidator(this.appValidEmail) : null
  }
 
}

export function EmaileValidator(email: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const forbidden = validMail(email)
    return forbidden ? {email: {value: control.value}} : null;
  };
}

export function validMail(mail: string){
  return /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()\.,;\s@\"]+\.{0,1})+([^<>()\.,;:\s@\"]{2,}|[\d\.]+))$/.test(mail);
}


