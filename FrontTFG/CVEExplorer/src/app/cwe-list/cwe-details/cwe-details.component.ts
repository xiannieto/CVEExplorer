import { Component, OnInit } from '@angular/core';
import { CweService } from '../../services/cwe-service.service';
import { CWE } from '../../models/cwe.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-cwe-details',
  templateUrl: './cwe-details.component.html',
  styleUrls: ['./cwe-details.component.css'],
})
export class CweDetailsComponent implements OnInit {
  cwe: CWE | null = null;
  ancestors: CWE[] = [];
  children: CWE[] = [];

  constructor(private route: ActivatedRoute, private cweService: CweService) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const cweId = params.get('id');
      if (cweId) {
        this.cweService.getCWEById(cweId).subscribe((cwe) => {
          this.cwe = cwe;
        });
        this.cweService.getAncestors(cweId).subscribe(
          (ancestors) => {
            // Asegurarse de que la propiedad ancestors siempre se inicialice con un valor válido
            this.ancestors = ancestors || [];
          },
          (error) => {
            // Manejar correctamente los códigos de estado HTTP 404
            if (error.status === 404) {
              this.ancestors = [];
            }
          }
        );
        this.cweService.getChildren(cweId).subscribe(
          (children) => {
            // Asegurarse de que la propiedad children siempre se inicialice con un valor válido
            this.children = children || [];
          },
          (error) => {
            // Manejar correctamente los códigos de estado HTTP 404
            if (error.status === 404) {
              this.children = [];
            }
          }
        );
      }
    });
  }

  get languagesArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.languages)) {
        return this.cwe.languages.filter((language) => language !== '');
      }
    }
    return [];
  }

  get technologiesArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.technologies)) {
        return this.cwe.technologies.filter((technology) => technology !== '');
      }
    }
    return [];
  }

  get operatingSystemsArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.operatingSystems)) {
        return this.cwe.operatingSystems.filter((os) => os !== '');
      }
    }
    return [];
  }

  get architecturesArray() {
    if (this.cwe) {
      if (Array.isArray(this.cwe.architectures)) {
        return this.cwe.architectures.filter(
          (architecture) => architecture !== ''
        );
      }
    }
    return [];
  }
}
