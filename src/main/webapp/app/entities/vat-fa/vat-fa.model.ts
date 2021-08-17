import * as dayjs from 'dayjs';
import { VatType } from 'app/entities/enumerations/vat-type.model';

export interface IVatFa {
  id?: number;
  name?: string | null;
  vatType?: VatType | null;
  validFrom?: dayjs.Dayjs | null;
  validUntil?: dayjs.Dayjs | null;
  vatPercent?: number | null;
  inactiv?: boolean | null;
  newVatId?: number | null;
}

export class VatFa implements IVatFa {
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

export function getVatFaIdentifier(vat: IVatFa): number | undefined {
  return vat.id;
}
