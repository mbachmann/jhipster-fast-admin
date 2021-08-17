export interface ICatalogCategory {
  id?: number;
  remoteId?: number | null;
  name?: string;
  accountingAccountNumber?: string | null;
  usage?: number | null;
  inactiv?: boolean | null;
}

export class CatalogCategory implements ICatalogCategory {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string,
    public accountingAccountNumber?: string | null,
    public usage?: number | null,
    public inactiv?: boolean | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getCatalogCategoryIdentifier(catalogCategory: ICatalogCategory): number | undefined {
  return catalogCategory.id;
}
