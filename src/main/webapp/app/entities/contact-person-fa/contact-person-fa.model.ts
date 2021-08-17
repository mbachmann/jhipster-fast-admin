import * as dayjs from 'dayjs';
import { ICustomFieldValueFa } from 'app/entities/custom-field-value-fa/custom-field-value-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
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
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  placeOfOrigin?: string | null;
  citizenCountry1?: string | null;
  citizenCountry2?: string | null;
  socialSecurityNumber?: string | null;
  hobbies?: string | null;
  dailyWork?: string | null;
  contactAttribute01?: string | null;
  avatarContentType?: string | null;
  avatar?: string | null;
  imageType?: string | null;
  inactiv?: boolean | null;
  customFields?: ICustomFieldValueFa[] | null;
  contact?: IContactFa | null;
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
    public birthDate?: dayjs.Dayjs | null,
    public birthPlace?: string | null,
    public placeOfOrigin?: string | null,
    public citizenCountry1?: string | null,
    public citizenCountry2?: string | null,
    public socialSecurityNumber?: string | null,
    public hobbies?: string | null,
    public dailyWork?: string | null,
    public contactAttribute01?: string | null,
    public avatarContentType?: string | null,
    public avatar?: string | null,
    public imageType?: string | null,
    public inactiv?: boolean | null,
    public customFields?: ICustomFieldValueFa[] | null,
    public contact?: IContactFa | null
  ) {
    this.defaultPerson = this.defaultPerson ?? false;
    this.showTitle = this.showTitle ?? false;
    this.showDepartment = this.showDepartment ?? false;
    this.wantsNewsletter = this.wantsNewsletter ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactPersonFaIdentifier(contactPerson: IContactPersonFa): number | undefined {
  return contactPerson.id;
}
