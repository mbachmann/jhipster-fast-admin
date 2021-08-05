import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactRelationType } from 'app/entities/enumerations/contact-relation-type.model';

export interface IContactRelationMySuffix {
  id?: number;
  contactRelationType?: ContactRelationType | null;
  contacts?: IContactMySuffix[] | null;
}

export class ContactRelationMySuffix implements IContactRelationMySuffix {
  constructor(public id?: number, public contactRelationType?: ContactRelationType | null, public contacts?: IContactMySuffix[] | null) {}
}

export function getContactRelationMySuffixIdentifier(contactRelation: IContactRelationMySuffix): number | undefined {
  return contactRelation.id;
}
