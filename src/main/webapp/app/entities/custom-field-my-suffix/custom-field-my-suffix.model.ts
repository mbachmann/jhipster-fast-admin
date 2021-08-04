import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { DomainResource } from 'app/entities/enumerations/domain-resource.model';

export interface ICustomFieldMySuffix {
  id?: number;
  domainResource?: DomainResource | null;
  key?: string | null;
  name?: string | null;
  value?: string | null;
  contact?: IContactMySuffix | null;
}

export class CustomFieldMySuffix implements ICustomFieldMySuffix {
  constructor(
    public id?: number,
    public domainResource?: DomainResource | null,
    public key?: string | null,
    public name?: string | null,
    public value?: string | null,
    public contact?: IContactMySuffix | null
  ) {}
}

export function getCustomFieldMySuffixIdentifier(customField: ICustomFieldMySuffix): number | undefined {
  return customField.id;
}
