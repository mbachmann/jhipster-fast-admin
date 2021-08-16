import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactRelationType } from 'app/entities/enumerations/contact-relation-type.model';

export interface IContactRelationFa {
  id?: number;
  contactRelationType?: ContactRelationType | null;
  contacts?: IContactFa[] | null;
}

export class ContactRelationFa implements IContactRelationFa {
  constructor(public id?: number, public contactRelationType?: ContactRelationType | null, public contacts?: IContactFa[] | null) {}
}

export function getContactRelationFaIdentifier(contactRelation: IContactRelationFa): number | undefined {
  return contactRelation.id;
}
