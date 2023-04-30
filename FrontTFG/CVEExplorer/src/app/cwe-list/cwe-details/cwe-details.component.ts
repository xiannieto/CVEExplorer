import { Component, OnInit } from '@angular/core';
import { CweService } from '../../services/cwe-service.service';
import { CWE } from '../../models/cwe.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-cwe-details',
  templateUrl: './cwe-details.component.html',
  styleUrls: ['./cwe-details.component.css']
})

export class CweDetailsComponent implements OnInit {
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
    if (this.cwe) {
      if (Array.isArray(this.cwe.languages)) {
        return this.cwe.languages.filter(language => language !== '');
      }
    }
    return [];
  }

  get technologiesArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.technologies)) {
        return this.cwe.technologies.filter(technology => technology !== '');
      }
    }
    return [];
  }

  get operatingSystemsArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.operatingSystems)) {
        return this.cwe.operatingSystems.filter(os => os !== '');
      }
    }
    return [];
  }

  get architecturesArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.architectures)) {
        return this.cwe.architectures.filter(architecture => architecture !== '');
      }
    }
    return [];
  }
}
