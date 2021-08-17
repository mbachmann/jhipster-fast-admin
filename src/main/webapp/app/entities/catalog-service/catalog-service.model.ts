import * as dayjs from 'dayjs';
import { ICustomFieldValue } from 'app/entities/custom-field-value/custom-field-value.model';
import { ICatalogCategory } from 'app/entities/catalog-category/catalog-category.model';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { IValueAddedTax } from 'app/entities/value-added-tax/value-added-tax.model';

export interface ICatalogService {
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
  defaultAmount?: number | null;
  created?: dayjs.Dayjs | null;
  inactiv?: boolean | null;
  customFields?: ICustomFieldValue[] | null;
  category?: ICatalogCategory | null;
  unit?: ICatalogUnit | null;
  valueAddedTax?: IValueAddedTax | null;
}

export class CatalogService implements ICatalogService {
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
    public defaultAmount?: number | null,
    public created?: dayjs.Dayjs | null,
    public inactiv?: boolean | null,
    public customFields?: ICustomFieldValue[] | null,
    public category?: ICatalogCategory | null,
    public unit?: ICatalogUnit | null,
    public valueAddedTax?: IValueAddedTax | null
  ) {
    this.includingVat = this.includingVat ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getCatalogServiceIdentifier(catalogService: ICatalogService): number | undefined {
  return catalogService.id;
}
