export class QueryResultsDTO {
  resultCount!: number;
  maxScore!: number;
  results!: ResultPair[];

  constructor() {}
}

export class ResultPair {
  cveID!: string;
  score!: number;

  constructor() {}
}
