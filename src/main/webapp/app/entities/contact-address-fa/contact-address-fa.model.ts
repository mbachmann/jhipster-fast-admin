import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { Country } from 'app/entities/enumerations/country.model';

export interface IContactAddressFa {
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
  contact?: IContactFa | null;
}

export class ContactAddressFa implements IContactAddressFa {
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
    public contact?: IContactFa | null
  ) {
    this.defaultAddress = this.defaultAddress ?? false;
    this.hideOnDocuments = this.hideOnDocuments ?? false;
    this.defaultPrepage = this.defaultPrepage ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactAddressFaIdentifier(contactAddress: IContactAddressFa): number | undefined {
  return contactAddress.id;
}
