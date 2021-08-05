import * as dayjs from 'dayjs';
import { IContactAddressMySuffix } from 'app/entities/contact-address-my-suffix/contact-address-my-suffix.model';
import { IPermissionMySuffix } from 'app/entities/permission-my-suffix/permission-my-suffix.model';
import { IContactGroupMySuffix } from 'app/entities/contact-group-my-suffix/contact-group-my-suffix.model';
import { ICustomFieldMySuffix } from 'app/entities/custom-field-my-suffix/custom-field-my-suffix.model';
import { IContactRelationMySuffix } from 'app/entities/contact-relation-my-suffix/contact-relation-my-suffix.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';
import { CommunicationChannel } from 'app/entities/enumerations/communication-channel.model';
import { CommunicationNewsletter } from 'app/entities/enumerations/communication-newsletter.model';
import { DiscountRate } from 'app/entities/enumerations/discount-rate.model';

export interface IContactMySuffix {
  id?: number;
  remoteId?: number | null;
  number?: string;
  type?: ContactType;
  gender?: GenderType;
  genderSalutationActive?: boolean;
  name?: string;
  nameAddition?: string | null;
  salutation?: string | null;
  phone?: string | null;
  fax?: string | null;
  email?: string | null;
  website?: string | null;
  notes?: string | null;
  communicationLanguage?: string | null;
  communicationChannel?: CommunicationChannel | null;
  communicationNewsletter?: CommunicationNewsletter | null;
  currency?: string | null;
  ebillAccountId?: string | null;
  vatIdentification?: string | null;
  vatRate?: number | null;
  discountRate?: number | null;
  discountType?: DiscountRate | null;
  paymentGrace?: number | null;
  hourlyRate?: number | null;
  created?: dayjs.Dayjs | null;
  mainAddressId?: number | null;
  mainAddress?: IContactAddressMySuffix | null;
  permissions?: IPermissionMySuffix[] | null;
  groups?: IContactGroupMySuffix[] | null;
  customFields?: ICustomFieldMySuffix[] | null;
  relations?: IContactRelationMySuffix[] | null;
}

export class ContactMySuffix implements IContactMySuffix {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string,
    public type?: ContactType,
    public gender?: GenderType,
    public genderSalutationActive?: boolean,
    public name?: string,
    public nameAddition?: string | null,
    public salutation?: string | null,
    public phone?: string | null,
    public fax?: string | null,
    public email?: string | null,
    public website?: string | null,
    public notes?: string | null,
    public communicationLanguage?: string | null,
    public communicationChannel?: CommunicationChannel | null,
    public communicationNewsletter?: CommunicationNewsletter | null,
    public currency?: string | null,
    public ebillAccountId?: string | null,
    public vatIdentification?: string | null,
    public vatRate?: number | null,
    public discountRate?: number | null,
    public discountType?: DiscountRate | null,
    public paymentGrace?: number | null,
    public hourlyRate?: number | null,
    public created?: dayjs.Dayjs | null,
    public mainAddressId?: number | null,
    public mainAddress?: IContactAddressMySuffix | null,
    public permissions?: IPermissionMySuffix[] | null,
    public groups?: IContactGroupMySuffix[] | null,
    public customFields?: ICustomFieldMySuffix[] | null,
    public relations?: IContactRelationMySuffix[] | null
  ) {
    this.genderSalutationActive = this.genderSalutationActive ?? false;
  }
}

export function getContactMySuffixIdentifier(contact: IContactMySuffix): number | undefined {
  return contact.id;
}
