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
  ancestors: string[] = [];
  children: CWE[] = [];

  constructor(
    private route: ActivatedRoute,
    private cweService: CweService
  ) {}

  ngOnInit() {
    const cweId = this.route.snapshot.paramMap.get('id');
    if (cweId) {
      this.cweService.getCWEById(cweId).subscribe(cwe => {
        this.cwe = cwe;
      });
      this.cweService.getAncestors(cweId).subscribe(ancestors => {
        this.ancestors = ancestors;
      });
      this.cweService.getChildren(cweId).subscribe(
        children => {
          this.children = children;
        },
        error => {
          if (error.status === 404) {
            console.error(
              'No se han encontrado hijos para el CWE seleccionado.',
              error.error
            );
          }
        }
      );
    }
  }
}
