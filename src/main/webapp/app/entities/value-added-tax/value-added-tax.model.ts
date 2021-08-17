import * as dayjs from 'dayjs';
import { VatType } from 'app/entities/enumerations/vat-type.model';

export interface IValueAddedTax {
  id?: number;
  name?: string | null;
  vatType?: VatType | null;
  validFrom?: dayjs.Dayjs | null;
  validUntil?: dayjs.Dayjs | null;
  vatPercent?: number | null;
  inactiv?: boolean | null;
  newVatId?: number | null;
}

export class ValueAddedTax implements IValueAddedTax {
  constructor(
    public id?: number,
    public name?: string | null,
    public vatType?: VatType | null,
    public validFrom?: dayjs.Dayjs | null,
    public validUntil?: dayjs.Dayjs | null,
    public vatPercent?: number | null,
    public inactiv?: boolean | null,
    public newVatId?: number | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getValueAddedTaxIdentifier(valueAddedTax: IValueAddedTax): number | undefined {
  return valueAddedTax.id;
}
