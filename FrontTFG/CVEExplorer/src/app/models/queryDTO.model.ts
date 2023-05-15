export class QueryDTO {
  description!: string;
  assigner!: string[];
  cwes!: string[];
  vendors!: string[];
  vendorProductPairs!: string[];
  attackVectors!: string[];

  constructor() {
    this.description = '';
    this.assigner = [];
    this.cwes = [];
    this.vendors = [];
    this.vendorProductPairs = [];
    this.attackVectors = [];
  }
}
