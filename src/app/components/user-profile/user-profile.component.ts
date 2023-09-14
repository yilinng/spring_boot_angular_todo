import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from './../../shared/auth.service';
import { TodoService } from 'src/app/Service/todo.service';
import { UserEntry, TodoEntry } from '../../types/types';
//import { map, filter } from 'rxjs/operators'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import {
  trigger,
  state,
  style,
  animate,
  transition,
  keyframes,
} from '@angular/animations';

@Component({
  selector: 'app-user-profile',
  animations: [
    trigger('openClose', [
      // ...
      state('open', style({
        height: '300px',
        opacity: 1,
      //  backgroundColor: 'yellow'
      })),
      state('closed', style({
        height: '0',
        opacity: 0.1,
      //  backgroundColor: 'blue'
      })),
      /*
      transition('* => *', [
        animate('1s', keyframes ( [
          style({ opacity: 0.1, offset: 0.1 }),
        //  style({ opacity: 0.6, offset: 0.2 }),
          style({ opacity: 1,   offset: 0.5 }),
        //  style({ opacity: 0.2, offset: 0.7 })
        ]))
      ]),
      */
      transition('open => closed', [
        animate('1s')
      ]),
      transition('closed => open', [
        animate('0.5s')
      ]),
      
    ]),
    trigger('myInsertRemoveTrigger', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('100ms', style({ opacity: 1 })),
      ]),
      transition(':leave', [
        animate('100ms', style({ opacity: 0 }))
      ])
    ]),
  ],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
  
//https://stackoverflow.com/questions/44900769/angular-wait-until-i-receive-data-before-loading-template  
export class UserProfileComponent implements OnInit {
  currentUser: UserEntry | undefined;
  todos: TodoEntry[] = [];
  clickTodo: Boolean = false;
  showUser: Boolean = true;

  addTodoForm: FormGroup;

  constructor(
    private authService: AuthService,
    private todoService: TodoService,
    private actRoute: ActivatedRoute,
    public fb: FormBuilder,
  ) { 
    this.addTodoForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      user: [this.currentUser && this.currentUser.id, Validators.required]
    });
  }
  ngOnInit() {
    this.getUser();
 //   this.getTodos();
  }

  getUser(): void {
   // let id = localStorage.getItem('user_id');

    //console.log('getUser id', id);

    this.authService.getUserProfile()
      .subscribe((res) => {
        this.currentUser = res;
        this.getTodos();
      });
  }

  getTodos() {
    return this.todoService.getTodos().subscribe((data) => {

    //  console.log('get todos data', data)

      const filteredTodos = data.filter(todo => this.currentUser && this.currentUser.todos?.includes(todo.id))
      this.todos = filteredTodos

    //  console.log('this.currentUser', this.currentUser)

    //  console.log('filteredTodos', filteredTodos)


    //  console.log('filteredTodos this.todos', this.todos)

    })
  }

  submitted = false;

  onSubmit() {
    this.submitted = true;
    this.todoService.addTodo(this.addTodoForm.value).subscribe((todo) => this.todos.push(todo));
  }

  delete(todo: TodoEntry): void {
    if (window.confirm('Want to delete?')) {
      this.todos = this.todos.filter((h) => h !== todo);
     // console.log('hero delete', todo)
      this.todoService.deleteTodo(todo).subscribe();
    }

  }

  showTodo() {
    this.showUser = false
    this.clickTodo = true
  }

  clickUser() {
    this.clickTodo = false
    this.showUser = true
  }

  checkValue() {
    if (this.addTodoForm.value.title && this.addTodoForm.value.content) {
      return false
    }
    return true
  }

  newTodo() {
    this.addTodoForm.reset()
  }

  truncateString(str: string, num: number) {
    if (str.length > num) {
      return str.slice(0, num) + "...";
    } else {
      return str;
    }
  }


}
