import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';

export interface IContactGroupFa {
  id?: number;
  remoteId?: number | null;
  name?: string;
  usage?: number | null;
  contacts?: IContactFa[] | null;
}

export class ContactGroupFa implements IContactGroupFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string,
    public usage?: number | null,
    public contacts?: IContactFa[] | null
  ) {}
}

export function getContactGroupFaIdentifier(contactGroup: IContactGroupFa): number | undefined {
  return contactGroup.id;
}
