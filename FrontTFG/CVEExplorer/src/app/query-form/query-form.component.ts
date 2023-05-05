import { Component, OnInit } from '@angular/core';
import { QueryService } from '../services/query-service.service';
import { QueryDTO } from '../models/queryDTO.model';
import { QueryResultsDTO } from '../models/queryResultDTO.model';

@Component({
  selector: 'app-query-form',
  templateUrl: './query-form.component.html',
  styleUrls: ['./query-form.component.css']
})
export class QueryFormComponent implements OnInit {
  cwes: string[] = [];
  vendors: string[] = [];
  vendorProductPairs: string[] = [];
  attackVectors: string[] = [];
  queryDTO: QueryDTO = new QueryDTO();
  queryResultsDTO!: QueryResultsDTO | undefined;

  constructor(private queryService: QueryService) {}

  ngOnInit() {
    this.queryService.getQueryCwes().subscribe(data => {
      this.cwes = data;
    });
  }

  onSubmit() {
    if (typeof this.queryDTO.cwes === 'string') {
      this.queryDTO.cwes = [this.queryDTO.cwes];
    }

    this.queryService.search(this.queryDTO).subscribe((data: QueryResultsDTO) => {
      this.queryResultsDTO = data;
    });
  }

  onReset() {
    this.queryDTO = new QueryDTO();
    this.queryResultsDTO = undefined as QueryResultsDTO | undefined;
  }
}
