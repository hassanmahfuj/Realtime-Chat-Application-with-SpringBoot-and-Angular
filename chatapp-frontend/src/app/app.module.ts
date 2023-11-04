import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { StompService } from './services/stomp.service';
import { ChatComponent } from './components/chat/chat.component';
import { AuthComponent } from './components/auth/auth.component';
import { FormsModule } from '@angular/forms';
import { BetterDatePipe } from './better-date.pipe';

@NgModule({
  declarations: [AppComponent, NotFoundComponent, ChatComponent, AuthComponent, BetterDatePipe],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule],
  providers: [StompService],
  bootstrap: [AppComponent],
})
export class AppModule {}
