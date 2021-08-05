import * as dayjs from 'dayjs';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { IPermissionFa } from 'app/entities/permission-fa/permission-fa.model';
import { IContactGroupFa } from 'app/entities/contact-group-fa/contact-group-fa.model';
import { ICustomFieldFa } from 'app/entities/custom-field-fa/custom-field-fa.model';
import { IContactRelationFa } from 'app/entities/contact-relation-fa/contact-relation-fa.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';
import { CommunicationChannel } from 'app/entities/enumerations/communication-channel.model';
import { CommunicationNewsletter } from 'app/entities/enumerations/communication-newsletter.model';
import { DiscountRate } from 'app/entities/enumerations/discount-rate.model';

export interface IContactFa {
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
  contactMainAddress?: IContactAddressFa | null;
  permissions?: IPermissionFa[] | null;
  groups?: IContactGroupFa[] | null;
  customFields?: ICustomFieldFa[] | null;
  relations?: IContactRelationFa[] | null;
}

export class ContactFa implements IContactFa {
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
    public contactMainAddress?: IContactAddressFa | null,
    public permissions?: IPermissionFa[] | null,
    public groups?: IContactGroupFa[] | null,
    public customFields?: ICustomFieldFa[] | null,
    public relations?: IContactRelationFa[] | null
  ) {
    this.genderSalutationActive = this.genderSalutationActive ?? false;
  }
}

export function getContactFaIdentifier(contact: IContactFa): number | undefined {
  return contact.id;
}
