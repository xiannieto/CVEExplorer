import { Component, OnInit } from '@angular/core';
import { CweService } from '../../services/cwe-service.service';
import { CWE } from '../../models/cwe.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-cwe-details',
  templateUrl: './cwe-details.component.html',
  styleUrls: ['./cwe-details.component.css']
})export class CweDetailsComponent implements OnInit {
  cwe: CWE | null = null;
  ancestors: CWE[] = [];
  children: CWE[] = [];

  constructor(
    private route: ActivatedRoute,
    private cweService: CweService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const cweId = params.get('id');
      if (cweId) {
        this.cweService.getCWEById(cweId).subscribe(cwe => {
          this.cwe = cwe;
        });
        this.cweService.getAncestors(cweId).subscribe(ancestors => {
          this.ancestors = ancestors;
        });
        this.cweService.getChildren(cweId).subscribe(children => {
            this.children = children;
        });
      }
    });
  }

  get languagesArray() {
    return this.cwe ? Array.from(this.cwe.languages) : [];
  }

  get technologiesArray() {
    return this.cwe ? Array.from(this.cwe.technologies) : [];
  }

  get operatingSystemsArray() {
    return this.cwe ? Array.from(this.cwe.operatingSystems) : [];
  }

  get architecturesArray() {
    return this.cwe ? Array.from(this.cwe.architectures) : [];
  }

  logTechnologiesInfo() {
    console.log('cwe.technologies.size:', this.cwe?.technologies.size);
    console.log('technologiesArray:', this.technologiesArray);
  }
  logCWE() {
    console.log(this.cwe);
  }
  logTechnologiesSize() {
    console.log('cwe.technologies.size:', this.cwe?.technologies.size);
  }

}
