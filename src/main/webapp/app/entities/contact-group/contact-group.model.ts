import { IContact } from 'app/entities/contact/contact.model';

export interface IContactGroup {
  id?: number;
  remoteId?: number | null;
  name?: string;
  usage?: number | null;
  contacts?: IContact[] | null;
}

export class ContactGroup implements IContactGroup {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string,
    public usage?: number | null,
    public contacts?: IContact[] | null
  ) {}
}

export function getContactGroupIdentifier(contactGroup: IContactGroup): number | undefined {
  return contactGroup.id;
}
