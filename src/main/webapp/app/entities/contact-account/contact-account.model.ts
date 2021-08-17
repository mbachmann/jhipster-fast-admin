import { IContact } from 'app/entities/contact/contact.model';
import { AccountType } from 'app/entities/enumerations/account-type.model';

export interface IContactAccount {
  id?: number;
  remoteId?: number | null;
  defaultAccount?: boolean | null;
  type?: AccountType | null;
  number?: string | null;
  bic?: string | null;
  description?: string | null;
  inactiv?: boolean | null;
  contact?: IContact | null;
}

export class ContactAccount implements IContactAccount {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public defaultAccount?: boolean | null,
    public type?: AccountType | null,
    public number?: string | null,
    public bic?: string | null,
    public description?: string | null,
    public inactiv?: boolean | null,
    public contact?: IContact | null
  ) {
    this.defaultAccount = this.defaultAccount ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactAccountIdentifier(contactAccount: IContactAccount): number | undefined {
  return contactAccount.id;
}
