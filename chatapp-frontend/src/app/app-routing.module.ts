import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { ChatComponent } from './components/chat/chat.component';
import { AuthComponent } from './components/auth/auth.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  // Default route for the home page, displaying the AuthComponent
  { path: '', component: AuthComponent },
  {
    // Route for the chat page, protected by the AuthGuard
    path: 'chat',
    canActivate: [AuthGuard],
    component: ChatComponent,
  },
  // Wildcard route to handle any undefined routes, displaying the NotFoundComponent
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
