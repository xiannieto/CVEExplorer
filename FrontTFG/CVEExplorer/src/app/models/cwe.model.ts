export class CWE {
  cweID!: string;
  name!: string;
  description!: string;
  extendedDescription!: string;
  children!: Set<string>;
  parents!: Set<string>;
  languages!: Set<string>;
  technologies!: Set<string>;
  operatingSystems!: Set<string>;
  architectures!: Set<string>;

  constructor(data: any) {
    this.cweID = data.cweID;
    this.name = data.name;
    this.description = data.description;
    this.extendedDescription = data.extendedDescription;
    this.children = data.children;
    this.parents = data.parents;
    this.languages = data.languages;
    this.technologies = data.technologies;
    this.operatingSystems = data.operatingSystems;
    this.architectures = data.architectures;
  }
}

