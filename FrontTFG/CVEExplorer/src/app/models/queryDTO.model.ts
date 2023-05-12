export class QueryDTO {
  assigner!: string[];
  cwes!: string[];
  vendors!: string[];
  vendorProductPairs!: string[];
  attackVectors!: string[];

  constructor() {
    this.assigner = [];
    this.cwes = [];
    this.vendors = [];
    this.vendorProductPairs = [];
    this.attackVectors = [];
  }
}
