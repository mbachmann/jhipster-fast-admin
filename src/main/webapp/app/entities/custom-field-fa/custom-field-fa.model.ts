import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';

export interface ICustomFieldFa {
  id?: number;
  domainArea?: DomainArea | null;
  key?: string | null;
  name?: string | null;
  value?: string | null;
  contact?: IContactFa | null;
  contactPerson?: IContactPersonFa | null;
}

export class CustomFieldFa implements ICustomFieldFa {
  constructor(
    public id?: number,
    public domainArea?: DomainArea | null,
    public key?: string | null,
    public name?: string | null,
    public value?: string | null,
    public contact?: IContactFa | null,
    public contactPerson?: IContactPersonFa | null
  ) {}
}

export function getCustomFieldFaIdentifier(customField: ICustomFieldFa): number | undefined {
  return customField.id;
}
