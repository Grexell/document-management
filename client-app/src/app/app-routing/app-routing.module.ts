import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from '../login/login.component';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {ErrorPageComponent} from '../error-page/error-page.component';
import {AnonymousGuard} from '../service/anonymous.guard';
import {AuthGuard} from '../service/auth.guard';
import {DocumentListComponent} from '../document-list/document-list.component';
import {DocumentCreateComponent} from '../document-create/document-create.component';

const ROUTES: Routes = [
  {path: 'login', component: LoginComponent, canActivate: [AnonymousGuard]},
  {
    path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard], canActivateChild: [AuthGuard], children: [
      {path: 'create', component: DocumentCreateComponent},
      {path: ':command', component: DocumentListComponent},
      {path: '', redirectTo: 'me', pathMatch: 'full'}
    ]
  },
  {path: '', redirectTo: '/dashboard/me', pathMatch: 'full'},
  {path: '**', component: ErrorPageComponent}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule, RouterModule.forRoot(ROUTES, {enableTracing: true})
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
