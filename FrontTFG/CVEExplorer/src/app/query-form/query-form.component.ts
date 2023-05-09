import { Component, OnInit } from '@angular/core';
import { QueryService } from '../services/query-service.service';
import { QueryDTO } from '../models/queryDTO.model';
import { QueryResultsDTO } from '../models/queryResultDTO.model';

@Component({
  selector: 'app-query-form',
  templateUrl: './query-form.component.html',
  styleUrls: ['./query-form.component.css'],
})
export class QueryFormComponent implements OnInit {
  cwes: string[] = [];
  assigners: string[] = [];
  vendorProductPairs: string[] = [];
  attackVectors: string[] = [];
  queryDTO: QueryDTO = new QueryDTO();
  queryResultsDTO!: QueryResultsDTO | undefined;
  descriptions: any[] = [];
  filteredDescriptions: any[] = [];
  vendors: any[] = [];
  filteredVendors: any[] = [];
  selectedVendor: string | null = null;

  constructor(private queryService: QueryService) {
    this.queryDTO.cwes = [];
    this.queryDTO.assigner = [];
    this.queryDTO.attackVectors = [];
  }

  ngOnInit() {
    this.queryService.getQueryCwes().subscribe((data) => {
      this.cwes = data;
    });
    this.queryService.getQueryAssigners().subscribe((data) => {
      this.assigners = data;
    });
    this.queryService.getQueryAttackVectors().subscribe((data) => {
      this.attackVectors = data;
    });
    this.queryService.getDescriptions().subscribe((data) => {
      this.descriptions = data;
    });
    this.queryService.getVendors().subscribe((data) => {
      this.vendors = data;
      console.log(this.vendors);
    });
  }

  onSelectVendor(event: any) {
    this.queryDTO.vendors.push(event);
    this.selectedVendor = null;
  }

  filterDescription(event: any) {
    let query = event.query;
    this.filteredDescriptions = this.descriptions.filter((description) => {
      return description.toLowerCase().indexOf(query.toLowerCase()) == 0;
    });
  }

  filterVendors(event: any) {
    let query = event.query;
    this.filteredVendors = this.vendors.filter((vendor) => {
      return vendor.toLowerCase().indexOf(query.toLowerCase()) == 0;
    });
    console.log(this.filteredVendors);
  }

  onSubmit() {
    const queryData: QueryDTO = {
      description: '',
      assigner: [],
      cwes: [],
      vendors: [],
      vendorProductPairs: [],
      attackVectors: [],
    };
    if (this.queryDTO.cwes.length > 0) {
      queryData.cwes = this.queryDTO.cwes;
    }
    if (this.queryDTO.assigner.length > 0) {
      queryData.assigner = this.queryDTO.assigner;
    }
    if (this.queryDTO.attackVectors.length > 0) {
      queryData.attackVectors = this.queryDTO.attackVectors;
    }

    this.queryService.search(queryData).subscribe((data: QueryResultsDTO) => {
      this.queryResultsDTO = data;
      console.log(this.queryResultsDTO);
    });
  }

  onReset() {
    this.queryDTO = new QueryDTO();
    this.queryResultsDTO = undefined as QueryResultsDTO | undefined;
  }
}
