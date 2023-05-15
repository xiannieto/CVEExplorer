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
  vendors: any[] = [];
  selectedVendors: string[] = [];
  filteredVendors: any[] = [];
  selectedAssigners: string[] = [];
  filteredAssigners: any[] = [];
  selectedCwes: string[] = [];
  filteredCwes: any[] = [];
  selectedAttackVectors: string[] = [];
  filteredAttackVectors: any[] = [];

  constructor(private queryService: QueryService) {
    this.queryDTO.cwes = [];
    this.queryDTO.assigner = [];
    this.queryDTO.attackVectors = [];
    this.queryDTO.description = '';
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
    this.queryService.getVendors().subscribe((data) => {
      this.vendors = data;
    });
  }

  filterAssigners(event: any) {
    let query = event.query;
    this.filteredAssigners = this.assigners.filter((assigner) => {
      return assigner.toLowerCase().indexOf(query.toLowerCase()) == 0;
    });
  }

  filterCwes(event: any) {
    let query = event.query;
    this.filteredCwes = this.cwes.filter((cwe) => {
      return cwe.toLowerCase().indexOf(query.toLowerCase()) == 0;
    });
  }

  filterVendors(event: any) {
    let query = event.query;
    this.filteredVendors = this.vendors.filter((vendor) => {
      return vendor.toLowerCase().indexOf(query.toLowerCase()) == 0;
    });
  }

  filterAttackVectors(event: any) {
    let query = event.query;
    this.filteredAttackVectors = this.attackVectors.filter((attackVector) => {
      return attackVector.toLowerCase().indexOf(query.toLowerCase()) == 0;
    });
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.key == 'Enter') {
      let tokenInput = event.srcElement as any;
      if (tokenInput.value) {
        this.selectedVendors.push(tokenInput.value);
        tokenInput.value = '';
      }
    }
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

    if (this.selectedAssigners.length > 0) {
      queryData.assigner = this.selectedAssigners;
    }

    if (this.selectedCwes.length > 0) {
      queryData.cwes = this.selectedCwes;
    }

    if (this.selectedAttackVectors.length > 0) {
      queryData.attackVectors = this.selectedAttackVectors;
    }

    if (this.selectedVendors.length > 0) {
      queryData.vendors = this.selectedVendors;
    }

    queryData.description = this.queryDTO.description;

    this.queryService.search(queryData).subscribe((data: QueryResultsDTO) => {
      this.queryResultsDTO = data;
    });
  }

  onReset() {
    this.queryDTO.description = '';

    this.queryDTO.assigner = [];

    this.queryDTO.cwes = [];

    this.queryDTO.attackVectors = [];

    this.queryResultsDTO = undefined as QueryResultsDTO | undefined;

    this.selectedAssigners = [];

    this.selectedCwes = [];

    this.selectedAttackVectors = [];

    this.selectedVendors = [];
  }
}
