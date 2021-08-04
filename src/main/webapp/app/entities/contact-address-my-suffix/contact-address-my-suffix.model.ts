import { IPermissionMySuffix } from 'app/entities/permission-my-suffix/permission-my-suffix.model';

export interface IContactAddressMySuffix {
  id?: number;
  defaultAddress?: boolean;
  country?: string;
  street?: string | null;
  streetNo?: string | null;
  street2?: string | null;
  postcode?: string | null;
  city?: string | null;
  hideOnDocuments?: boolean;
  defaultPrepage?: boolean;
  permissions?: IPermissionMySuffix[] | null;
}

export class ContactAddressMySuffix implements IContactAddressMySuffix {
  constructor(
    public id?: number,
    public defaultAddress?: boolean,
    public country?: string,
    public street?: string | null,
    public streetNo?: string | null,
    public street2?: string | null,
    public postcode?: string | null,
    public city?: string | null,
    public hideOnDocuments?: boolean,
    public defaultPrepage?: boolean,
    public permissions?: IPermissionMySuffix[] | null
  ) {
    this.defaultAddress = this.defaultAddress ?? false;
    this.hideOnDocuments = this.hideOnDocuments ?? false;
    this.defaultPrepage = this.defaultPrepage ?? false;
  }
}

export function getContactAddressMySuffixIdentifier(contactAddress: IContactAddressMySuffix): number | undefined {
  return contactAddress.id;
}
