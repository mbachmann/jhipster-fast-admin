import { CompanyLanguage } from 'app/entities/enumerations/company-language.model';
import { Country } from 'app/entities/enumerations/country.model';
import { CompanyCurrency } from 'app/entities/enumerations/company-currency.model';

export interface IOwner {
  id?: number;
  remoteId?: number | null;
  name?: string | null;
  surname?: string | null;
  email?: string | null;
  language?: CompanyLanguage | null;
  companyName?: string | null;
  companyAddition?: string | null;
  companyCountry?: Country | null;
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
  companyCurrency?: CompanyCurrency | null;
}

export class Owner implements IOwner {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string | null,
    public surname?: string | null,
    public email?: string | null,
    public language?: CompanyLanguage | null,
    public companyName?: string | null,
    public companyAddition?: string | null,
    public companyCountry?: Country | null,
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
    public companyCurrency?: CompanyCurrency | null
  ) {}
}

export function getOwnerIdentifier(owner: IOwner): number | undefined {
  return owner.id;
}
