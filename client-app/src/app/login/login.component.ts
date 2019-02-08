import {Component, OnInit} from '@angular/core';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private loginForm = new FormGroup({
    login: new FormControl(''),
    password: new FormControl('')
  });

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
  }

  authenticate() {
    this.userService.authenticate(this.loginForm.value.login, this.loginForm.value.password).subscribe((data) => {
      if (data) {
        this.router.navigate(['/dashboard/me']);
      }
    });
  }
}
