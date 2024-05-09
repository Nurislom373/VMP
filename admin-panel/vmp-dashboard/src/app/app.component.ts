import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';
import {NavbarComponent} from "./layout/navbar/navbar.component";
import {SidebarComponent} from "./layout/sidebar/sidebar.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  title = 'vmp-dashboard';

  ngOnInit(): void {
      initFlowbite();
  }
}
