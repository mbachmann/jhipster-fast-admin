import { IContact } from 'app/entities/contact/contact.model';
import { ContactRelationType } from 'app/entities/enumerations/contact-relation-type.model';

export interface IContactRelation {
  id?: number;
  contactRelationType?: ContactRelationType | null;
  contacts?: IContact[] | null;
}

export class ContactRelation implements IContactRelation {
  constructor(public id?: number, public contactRelationType?: ContactRelationType | null, public contacts?: IContact[] | null) {}
}

export function getContactRelationIdentifier(contactRelation: IContactRelation): number | undefined {
  return contactRelation.id;
}
