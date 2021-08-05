import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { IContactPersonMySuffix } from 'app/entities/contact-person-my-suffix/contact-person-my-suffix.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';

export interface ICustomFieldMySuffix {
  id?: number;
  domainArea?: DomainArea | null;
  key?: string | null;
  name?: string | null;
  value?: string | null;
  contact?: IContactMySuffix | null;
  contactPerson?: IContactPersonMySuffix | null;
}

export class CustomFieldMySuffix implements ICustomFieldMySuffix {
  constructor(
    public id?: number,
    public domainArea?: DomainArea | null,
    public key?: string | null,
    public name?: string | null,
    public value?: string | null,
    public contact?: IContactMySuffix | null,
    public contactPerson?: IContactPersonMySuffix | null
  ) {}
}

export function getCustomFieldMySuffixIdentifier(customField: ICustomFieldMySuffix): number | undefined {
  return customField.id;
}
