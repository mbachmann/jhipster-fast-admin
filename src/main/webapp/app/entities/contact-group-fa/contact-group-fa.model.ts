import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';

export interface IContactGroupFa {
  id?: number;
  name?: string;
  contact?: IContactFa | null;
}

export class ContactGroupFa implements IContactGroupFa {
  constructor(public id?: number, public name?: string, public contact?: IContactFa | null) {}
}

export function getContactGroupFaIdentifier(contactGroup: IContactGroupFa): number | undefined {
  return contactGroup.id;
}
