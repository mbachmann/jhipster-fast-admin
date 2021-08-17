import { CatalogScope } from 'app/entities/enumerations/catalog-scope.model';

export interface ICatalogUnit {
  id?: number;
  remoteId?: number | null;
  name?: string;
  nameDe?: string | null;
  nameEn?: string | null;
  nameFr?: string | null;
  nameIt?: string | null;
  scope?: CatalogScope | null;
  custom?: boolean | null;
  inactiv?: boolean | null;
}

export class CatalogUnit implements ICatalogUnit {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string,
    public nameDe?: string | null,
    public nameEn?: string | null,
    public nameFr?: string | null,
    public nameIt?: string | null,
    public scope?: CatalogScope | null,
    public custom?: boolean | null,
    public inactiv?: boolean | null
  ) {
    this.custom = this.custom ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getCatalogUnitIdentifier(catalogUnit: ICatalogUnit): number | undefined {
  return catalogUnit.id;
}
