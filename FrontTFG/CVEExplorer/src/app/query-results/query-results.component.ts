import { Component, Input } from '@angular/core';
import { QueryResultsDTO } from '../models/queryResultDTO.model';

@Component({
  selector: 'app-query-results',
  templateUrl: './query-results.component.html',
  styleUrls: ['./query-results.component.css']
})
export class QueryResultsComponent {
  @Input() queryResults!: QueryResultsDTO | undefined;
}
