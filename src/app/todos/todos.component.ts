import { Component, OnInit } from '@angular/core';

import { TodoEntry } from '../types/types';

import { TodoService } from '../Service/todo.service';

import {
  trigger,
  state,
  style,
  animate,
  transition,
  keyframes,
} from '@angular/animations';

@Component({
  selector: 'app-todos',
  animations: [
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
  ],
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.css']
})
export class TodosComponent implements OnInit {
  todos: TodoEntry[] = [];

  constructor(private todoService: TodoService) { }

  ngOnInit(): void {
    this.getTodos()
  }

  getTodos(): void {
    this.todoService.getTodos().subscribe((todos) => (this.todos = todos));
  }

}
