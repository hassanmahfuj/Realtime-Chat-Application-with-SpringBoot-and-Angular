import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    // Check if a user is authenticated by checking if user data is stored in local storage
    if (localStorage.getItem('user') != null) {
      // User is authenticated, allow access to the route
      return true;
    }

    // User is not authenticated, redirect to the login page
    this.router.navigate(['']);
    return false;
  }
}
