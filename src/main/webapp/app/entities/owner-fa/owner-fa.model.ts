import { LanguageType } from 'app/entities/enumerations/language-type.model';
import { CurrencyType } from 'app/entities/enumerations/currency-type.model';

export interface IOwnerFa {
  id?: number;
  remoteId?: number | null;
  name?: string | null;
  surname?: string | null;
  email?: string | null;
  language?: LanguageType | null;
  companyName?: string | null;
  companyAddition?: string | null;
  companyCountry?: string | null;
  companyStreet?: string | null;
  companyStreetNo?: string | null;
  companyStreet2?: string | null;
  companyPostcode?: string | null;
  companyCity?: string | null;
  companyPhone?: string | null;
  companyFax?: string | null;
  companyEmail?: string | null;
  companyWebsite?: string | null;
  companyVatId?: string | null;
  companyCurrency?: CurrencyType | null;
}

export class OwnerFa implements IOwnerFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string | null,
    public surname?: string | null,
    public email?: string | null,
    public language?: LanguageType | null,
    public companyName?: string | null,
    public companyAddition?: string | null,
    public companyCountry?: string | null,
    public companyStreet?: string | null,
    public companyStreetNo?: string | null,
    public companyStreet2?: string | null,
    public companyPostcode?: string | null,
    public companyCity?: string | null,
    public companyPhone?: string | null,
    public companyFax?: string | null,
    public companyEmail?: string | null,
    public companyWebsite?: string | null,
    public companyVatId?: string | null,
    public companyCurrency?: CurrencyType | null
  ) {}
}

export function getOwnerFaIdentifier(owner: IOwnerFa): number | undefined {
  return owner.id;
}
