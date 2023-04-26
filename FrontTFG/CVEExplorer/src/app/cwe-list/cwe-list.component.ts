import { Component } from '@angular/core';
import { CweService } from '../services/cwe-service.service';
import { CWE } from '../models/cwe.model';

@Component({
  selector: 'app-cwe-list',
  templateUrl: './cwe-list.component.html',
  styleUrls: ['./cwe-list.component.css']
})
export class CweListComponent {
  cwes: Array<CWE> = [];

  constructor(private cweService: CweService) {}

  ngOnInit() {
    this.cweService.findRoots().subscribe(cweList => {
      this.cwes = cweList;
    });
  }
}
