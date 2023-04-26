export class Cve {
  cveID: string;
  assigner: string;
  description: string;
  cwes: string[];
  cwesWithAncestors: string[];
  cpes: string[];
  vendorProductPairs: string[];
  configurations: any[];
  references: any[];
  impact: any;

  constructor(data: any) {
    this.cveID = data.cveID;
    this.assigner = data.assigner;
    this.description = data.description;
    this.cwes = data.cwes;
    this.cwesWithAncestors = data.cwesWithAncestors;
    this.cpes = data.cpes;
    this.vendorProductPairs = data.vendorProductPairs;
    this.configurations = data.configurations;
    this.references = data.references;
    this.impact = data.impact;
  }
}
