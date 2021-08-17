import * as dayjs from 'dayjs';
import { ICustomFieldValueFa } from 'app/entities/custom-field-value-fa/custom-field-value-fa.model';
import { ICatalogCategoryFa } from 'app/entities/catalog-category-fa/catalog-category-fa.model';
import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { IVatFa } from 'app/entities/vat-fa/vat-fa.model';

export interface ICatalogProductFa {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
  name?: string;
  description?: string | null;
  notes?: string | null;
  categoryName?: string | null;
  includingVat?: boolean | null;
  vat?: number | null;
  unitName?: string | null;
  price?: number | null;
  pricePurchase?: number | null;
  inventoryAvailable?: number | null;
  inventoryReserved?: number | null;
  defaultAmount?: number | null;
  created?: dayjs.Dayjs | null;
  inactiv?: boolean | null;
  customFields?: ICustomFieldValueFa[] | null;
  category?: ICatalogCategoryFa | null;
  unit?: ICatalogUnitFa | null;
  vat?: IVatFa | null;
}

export class CatalogProductFa implements ICatalogProductFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
    public name?: string,
    public description?: string | null,
    public notes?: string | null,
    public categoryName?: string | null,
    public includingVat?: boolean | null,
    public vat?: number | null,
    public unitName?: string | null,
    public price?: number | null,
    public pricePurchase?: number | null,
    public inventoryAvailable?: number | null,
    public inventoryReserved?: number | null,
    public defaultAmount?: number | null,
    public created?: dayjs.Dayjs | null,
    public inactiv?: boolean | null,
    public customFields?: ICustomFieldValueFa[] | null,
    public category?: ICatalogCategoryFa | null,
    public unit?: ICatalogUnitFa | null,
    public vat?: IVatFa | null
  ) {
    this.includingVat = this.includingVat ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getCatalogProductFaIdentifier(catalogProduct: ICatalogProductFa): number | undefined {
  return catalogProduct.id;
}
