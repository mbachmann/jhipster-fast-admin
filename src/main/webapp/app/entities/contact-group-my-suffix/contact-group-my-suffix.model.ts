import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';

export interface IContactGroupMySuffix {
  id?: number;
  name?: string;
  contact?: IContactMySuffix | null;
}

export class ContactGroupMySuffix implements IContactGroupMySuffix {
  constructor(public id?: number, public name?: string, public contact?: IContactMySuffix | null) {}
}

export function getContactGroupMySuffixIdentifier(contactGroup: IContactGroupMySuffix): number | undefined {
  return contactGroup.id;
}
