import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { CveListComponent } from './cve-list/cve-list.component';
import { CweListComponent } from './cwe-list/cwe-list.component';
import { QueryFormComponent } from './query-form/query-form.component';
import { CveDetailsComponent } from './cve-list/cve-details/cve-details.component';
import { CweDetailsComponent } from './cwe-list/cwe-details/cwe-details.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'cves', component: CveListComponent },
  { path: 'cwes', component: CweListComponent },
  { path: 'query', component: QueryFormComponent },
  { path: 'cve/:id', component: CveDetailsComponent },
  { path: 'cwe/:id', component: CweDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
