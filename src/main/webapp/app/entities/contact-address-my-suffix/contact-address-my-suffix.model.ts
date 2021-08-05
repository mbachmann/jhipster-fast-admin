export interface IContactAddressMySuffix {
  id?: number;
  remoteId?: number | null;
  defaultAddress?: boolean;
  country?: string;
  street?: string | null;
  streetNo?: string | null;
  street2?: string | null;
  postcode?: string | null;
  city?: string | null;
  hideOnDocuments?: boolean;
  defaultPrepage?: boolean;
}

export class ContactAddressMySuffix implements IContactAddressMySuffix {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public defaultAddress?: boolean,
    public country?: string,
    public street?: string | null,
    public streetNo?: string | null,
    public street2?: string | null,
    public postcode?: string | null,
    public city?: string | null,
    public hideOnDocuments?: boolean,
    public defaultPrepage?: boolean
  ) {
    this.defaultAddress = this.defaultAddress ?? false;
    this.hideOnDocuments = this.hideOnDocuments ?? false;
    this.defaultPrepage = this.defaultPrepage ?? false;
  }
}

export function getContactAddressMySuffixIdentifier(contactAddress: IContactAddressMySuffix): number | undefined {
  return contactAddress.id;
}
