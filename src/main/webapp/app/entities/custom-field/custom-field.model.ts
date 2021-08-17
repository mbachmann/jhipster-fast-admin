import { DomainArea } from 'app/entities/enumerations/domain-area.model';

export interface ICustomField {
  id?: number;
  domainArea?: DomainArea | null;
  key?: string;
  name?: string;
  defaultValue?: string | null;
}

export class CustomField implements ICustomField {
  constructor(
    public id?: number,
    public domainArea?: DomainArea | null,
    public key?: string,
    public name?: string,
    public defaultValue?: string | null
  ) {}
}

export function getCustomFieldIdentifier(customField: ICustomField): number | undefined {
  return customField.id;
}
