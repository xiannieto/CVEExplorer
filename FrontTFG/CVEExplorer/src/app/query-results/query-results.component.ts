import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { QueryResultsDTO } from '../models/queryResultDTO.model';
import { Observable } from 'rxjs';
import { Cve } from '../models/cve.model';
import { CveService } from '../services/cve-service.service';

@Component({
  selector: 'app-query-results',
  templateUrl: './query-results.component.html',
  styleUrls: ['./query-results.component.css'],
})
export class QueryResultsComponent implements OnInit, OnChanges {
  @Input() queryResults!: QueryResultsDTO | undefined;
  cveDetails: Observable<Cve>[] = [];

  constructor(private cveService: CveService) {}

  ngOnInit(): void {
    this.updateCveDetails();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['queryResults']) {
      this.updateCveDetails();
    }
  }

  updateCveDetails(): void {
    if (this.queryResults) {
      this.cveDetails = this.queryResults.results.map((result) =>
        this.cveService.getCveById(result.cveID)
      );
    }
  }
}
