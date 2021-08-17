import * as dayjs from 'dayjs';
import { ICustomFieldValueFa } from 'app/entities/custom-field-value-fa/custom-field-value-fa.model';
import { IContactRelationFa } from 'app/entities/contact-relation-fa/contact-relation-fa.model';
import { IContactGroupFa } from 'app/entities/contact-group-fa/contact-group-fa.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';
import { CommunicationChannel } from 'app/entities/enumerations/communication-channel.model';
import { CommunicationNewsletter } from 'app/entities/enumerations/communication-newsletter.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';

export interface IContactFa {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
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
  currency?: Currency | null;
  ebillAccountId?: string | null;
  vatIdentification?: string | null;
  vatRate?: number | null;
  discountRate?: number | null;
  discountType?: DiscountType | null;
  paymentGrace?: number | null;
  hourlyRate?: number | null;
  created?: dayjs.Dayjs | null;
  mainAddressId?: number | null;
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
  relations?: IContactRelationFa[] | null;
  groups?: IContactGroupFa[] | null;
}

export class ContactFa implements IContactFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
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
    public currency?: Currency | null,
    public ebillAccountId?: string | null,
    public vatIdentification?: string | null,
    public vatRate?: number | null,
    public discountRate?: number | null,
    public discountType?: DiscountType | null,
    public paymentGrace?: number | null,
    public hourlyRate?: number | null,
    public created?: dayjs.Dayjs | null,
    public mainAddressId?: number | null,
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
    public relations?: IContactRelationFa[] | null,
    public groups?: IContactGroupFa[] | null
  ) {
    this.genderSalutationActive = this.genderSalutationActive ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactFaIdentifier(contact: IContactFa): number | undefined {
  return contact.id;
}
