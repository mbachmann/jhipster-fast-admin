import { ICustomFieldFa } from 'app/entities/custom-field-fa/custom-field-fa.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';

export interface IContactPersonFa {
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
  customFields?: ICustomFieldFa[] | null;
}

export class ContactPersonFa implements IContactPersonFa {
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
    public customFields?: ICustomFieldFa[] | null
  ) {
    this.defaultPerson = this.defaultPerson ?? false;
    this.showTitle = this.showTitle ?? false;
    this.showDepartment = this.showDepartment ?? false;
    this.wantsNewsletter = this.wantsNewsletter ?? false;
  }
}

export function getContactPersonFaIdentifier(contactPerson: IContactPersonFa): number | undefined {
  return contactPerson.id;
}
