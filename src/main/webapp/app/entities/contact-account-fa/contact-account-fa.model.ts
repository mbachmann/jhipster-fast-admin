import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { AccountType } from 'app/entities/enumerations/account-type.model';

export interface IContactAccountFa {
  id?: number;
  remoteId?: number | null;
  defaultAccount?: boolean | null;
  type?: AccountType | null;
  number?: string | null;
  bic?: string | null;
  description?: string | null;
  inactiv?: boolean | null;
  contact?: IContactFa | null;
}

export class ContactAccountFa implements IContactAccountFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public defaultAccount?: boolean | null,
    public type?: AccountType | null,
    public number?: string | null,
    public bic?: string | null,
    public description?: string | null,
    public inactiv?: boolean | null,
    public contact?: IContactFa | null
  ) {
    this.defaultAccount = this.defaultAccount ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactAccountFaIdentifier(contactAccount: IContactAccountFa): number | undefined {
  return contactAccount.id;
}
