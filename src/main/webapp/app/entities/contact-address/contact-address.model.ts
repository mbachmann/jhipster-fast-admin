import { IContact } from 'app/entities/contact/contact.model';
import { Country } from 'app/entities/enumerations/country.model';

export interface IContactAddress {
  id?: number;
  remoteId?: number | null;
  defaultAddress?: boolean;
  country?: Country;
  street?: string | null;
  streetNo?: string | null;
  street2?: string | null;
  postcode?: string | null;
  city?: string | null;
  hideOnDocuments?: boolean;
  defaultPrepage?: boolean;
  inactiv?: boolean | null;
  contact?: IContact | null;
}

export class ContactAddress implements IContactAddress {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public defaultAddress?: boolean,
    public country?: Country,
    public street?: string | null,
    public streetNo?: string | null,
    public street2?: string | null,
    public postcode?: string | null,
    public city?: string | null,
    public hideOnDocuments?: boolean,
    public defaultPrepage?: boolean,
    public inactiv?: boolean | null,
    public contact?: IContact | null
  ) {
    this.defaultAddress = this.defaultAddress ?? false;
    this.hideOnDocuments = this.hideOnDocuments ?? false;
    this.defaultPrepage = this.defaultPrepage ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactAddressIdentifier(contactAddress: IContactAddress): number | undefined {
  return contactAddress.id;
}
