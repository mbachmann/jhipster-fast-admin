import { ICustomFieldMySuffix } from 'app/entities/custom-field-my-suffix/custom-field-my-suffix.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';

export interface IContactPersonMySuffix {
  id?: number;
  remoteId?: number | null;
  defaultPerson?: boolean | null;
  name?: string | null;
  surname?: string | null;
  gender?: GenderType | null;
  email?: string | null;
  phone?: string | null;
  department?: string | null;
  salutation?: string | null;
  showTitle?: boolean | null;
  showDepartment?: boolean | null;
  wantsNewsletter?: boolean | null;
  customFields?: ICustomFieldMySuffix[] | null;
}

export class ContactPersonMySuffix implements IContactPersonMySuffix {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public defaultPerson?: boolean | null,
    public name?: string | null,
    public surname?: string | null,
    public gender?: GenderType | null,
    public email?: string | null,
    public phone?: string | null,
    public department?: string | null,
    public salutation?: string | null,
    public showTitle?: boolean | null,
    public showDepartment?: boolean | null,
    public wantsNewsletter?: boolean | null,
    public customFields?: ICustomFieldMySuffix[] | null
  ) {
    this.defaultPerson = this.defaultPerson ?? false;
    this.showTitle = this.showTitle ?? false;
    this.showDepartment = this.showDepartment ?? false;
    this.wantsNewsletter = this.wantsNewsletter ?? false;
  }
}

export function getContactPersonMySuffixIdentifier(contactPerson: IContactPersonMySuffix): number | undefined {
  return contactPerson.id;
}
