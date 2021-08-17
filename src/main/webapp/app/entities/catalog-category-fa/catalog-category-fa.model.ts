export interface ICatalogCategoryFa {
  id?: number;
  remoteId?: number | null;
  name?: string;
  accountingAccountNumber?: string | null;
  usage?: number | null;
  inactiv?: boolean | null;
}

export class CatalogCategoryFa implements ICatalogCategoryFa {
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

export function getCatalogCategoryFaIdentifier(catalogCategory: ICatalogCategoryFa): number | undefined {
  return catalogCategory.id;
}
