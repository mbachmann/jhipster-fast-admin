import * as dayjs from 'dayjs';
import { IContactAddressMySuffix } from 'app/entities/contact-address-my-suffix/contact-address-my-suffix.model';
import { IPermissionMySuffix } from 'app/entities/permission-my-suffix/permission-my-suffix.model';
import { IContactGroupMySuffix } from 'app/entities/contact-group-my-suffix/contact-group-my-suffix.model';
import { ICustomFieldMySuffix } from 'app/entities/custom-field-my-suffix/custom-field-my-suffix.model';
import { ContactRelation } from 'app/entities/enumerations/contact-relation.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';

export interface IContactMySuffix {
  id?: number;
  number?: string | null;
  relation?: ContactRelation | null;
  type?: ContactType | null;
  gender?: GenderType | null;
  genderSalutationActive?: boolean | null;
  name?: string | null;
  nameAddition?: string | null;
  salutation?: string | null;
  phone?: string | null;
  fax?: string | null;
  email?: string | null;
  website?: string | null;
  notes?: string | null;
  communicationLanguage?: string | null;
  communicationChannel?: string | null;
  communicationNewsletter?: string | null;
  currency?: string | null;
  ebillAccountId?: string | null;
  vatIdentification?: string | null;
  vatRate?: number | null;
  discountRate?: number | null;
  discountType?: string | null;
  paymentGrace?: number | null;
  hourlyRate?: number | null;
  created?: dayjs.Dayjs | null;
  mainAddressId?: number | null;
  mainAddress?: IContactAddressMySuffix | null;
  permissions?: IPermissionMySuffix[] | null;
  groups?: IContactGroupMySuffix[] | null;
  customFields?: ICustomFieldMySuffix[] | null;
}

export class ContactMySuffix implements IContactMySuffix {
  constructor(
    public id?: number,
    public number?: string | null,
    public relation?: ContactRelation | null,
    public type?: ContactType | null,
    public gender?: GenderType | null,
    public genderSalutationActive?: boolean | null,
    public name?: string | null,
    public nameAddition?: string | null,
    public salutation?: string | null,
    public phone?: string | null,
    public fax?: string | null,
    public email?: string | null,
    public website?: string | null,
    public notes?: string | null,
    public communicationLanguage?: string | null,
    public communicationChannel?: string | null,
    public communicationNewsletter?: string | null,
    public currency?: string | null,
    public ebillAccountId?: string | null,
    public vatIdentification?: string | null,
    public vatRate?: number | null,
    public discountRate?: number | null,
    public discountType?: string | null,
    public paymentGrace?: number | null,
    public hourlyRate?: number | null,
    public created?: dayjs.Dayjs | null,
    public mainAddressId?: number | null,
    public mainAddress?: IContactAddressMySuffix | null,
    public permissions?: IPermissionMySuffix[] | null,
    public groups?: IContactGroupMySuffix[] | null,
    public customFields?: ICustomFieldMySuffix[] | null
  ) {
    this.genderSalutationActive = this.genderSalutationActive ?? false;
  }
}

export function getContactMySuffixIdentifier(contact: IContactMySuffix): number | undefined {
  return contact.id;
}
