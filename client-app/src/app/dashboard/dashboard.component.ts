import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  username: string;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.username = this.userService.getDecodedAccessToken()['name'];
  }

  logout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}
