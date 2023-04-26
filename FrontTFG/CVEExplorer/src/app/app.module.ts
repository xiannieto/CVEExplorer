import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CveListComponent } from './cve-list/cve-list.component';
import { CveDetailsComponent } from './cve-list/cve-details/cve-details.component';
import { QueryFormComponent } from './query-form/query-form.component';
import { QueryResultsComponent } from './query-results/query-results.component';
import { CweListComponent } from './cwe-list/cwe-list.component';
import { CweDetailsComponent } from './cwe-list/cwe-details/cwe-details.component';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    CveListComponent,
    CveDetailsComponent,
    QueryFormComponent,
    QueryResultsComponent,
    CweListComponent,
    CweDetailsComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
