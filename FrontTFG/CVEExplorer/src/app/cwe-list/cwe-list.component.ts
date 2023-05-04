import { Component, OnInit } from '@angular/core';
import { CweService } from '../services/cwe-service.service';
import { CWE } from '../models/cwe.model';

@Component({
  selector: 'app-cwe-list',
  templateUrl: './cwe-list.component.html',
  styleUrls: ['./cwe-list.component.css']
})
export class CweListComponent implements OnInit{
  cwes: Array<CWE> = [];
  pageNumber: number = 0;
  pageSize: number = 20;
  totalPages: number = 0;
  Math = Math;

  constructor(private cweService: CweService) {}

  ngOnInit(): void {
    this.loadPage();
  }

  loadPage(): void {
    this.cweService.findRoots(this.pageNumber, this.pageSize).subscribe(cweList => {
      this.cwes = cweList.cwes;
      this.totalPages = Math.ceil(cweList.totalResults / this.pageSize);
    });
  }

  nextPage(): void {
    this.pageNumber++;
    this.loadPage();
  }

  previousPage(): void {
    if (this.pageNumber > 0) {
      this.pageNumber--;
      this.loadPage();
    }
  }
}
