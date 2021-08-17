import * as dayjs from 'dayjs';
import { IDocumentFreeText } from 'app/entities/document-free-text/document-free-text.model';
import { IDescriptiveDocumentText } from 'app/entities/descriptive-document-text/descriptive-document-text.model';
import { IDocumentPosition } from 'app/entities/document-position/document-position.model';
import { IContact } from 'app/entities/contact/contact.model';
import { IContactAddress } from 'app/entities/contact-address/contact-address.model';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ILayout } from 'app/entities/layout/layout.model';
import { ISignature } from 'app/entities/signature/signature.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { OfferAcceptOnlineStatus } from 'app/entities/enumerations/offer-accept-online-status.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { OfferStatus } from 'app/entities/enumerations/offer-status.model';

export interface IOffer {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
  contactName?: string | null;
  date?: dayjs.Dayjs | null;
  validUntil?: dayjs.Dayjs | null;
  periodText?: string | null;
  currency?: Currency | null;
  total?: number | null;
  vatIncluded?: boolean | null;
  discountRate?: number | null;
  discountType?: DiscountType | null;
  acceptOnline?: boolean | null;
  acceptOnlineUrl?: string | null;
  acceptOnlineStatus?: OfferAcceptOnlineStatus | null;
  language?: DocumentLanguage | null;
  pageAmount?: number | null;
  notes?: string | null;
  status?: OfferStatus | null;
  created?: dayjs.Dayjs | null;
  freeTexts?: IDocumentFreeText[] | null;
  texts?: IDescriptiveDocumentText[] | null;
  positions?: IDocumentPosition[] | null;
  contact?: IContact | null;
  contactAddress?: IContactAddress | null;
  contactPerson?: IContactPerson | null;
  contactPrePageAddress?: IContactAddress | null;
  layout?: ILayout | null;
  layout?: ISignature | null;
}

export class Offer implements IOffer {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
    public contactName?: string | null,
    public date?: dayjs.Dayjs | null,
    public validUntil?: dayjs.Dayjs | null,
    public periodText?: string | null,
    public currency?: Currency | null,
    public total?: number | null,
    public vatIncluded?: boolean | null,
    public discountRate?: number | null,
    public discountType?: DiscountType | null,
    public acceptOnline?: boolean | null,
    public acceptOnlineUrl?: string | null,
    public acceptOnlineStatus?: OfferAcceptOnlineStatus | null,
    public language?: DocumentLanguage | null,
    public pageAmount?: number | null,
    public notes?: string | null,
    public status?: OfferStatus | null,
    public created?: dayjs.Dayjs | null,
    public freeTexts?: IDocumentFreeText[] | null,
    public texts?: IDescriptiveDocumentText[] | null,
    public positions?: IDocumentPosition[] | null,
    public contact?: IContact | null,
    public contactAddress?: IContactAddress | null,
    public contactPerson?: IContactPerson | null,
    public contactPrePageAddress?: IContactAddress | null,
    public layout?: ILayout | null,
    public layout?: ISignature | null
  ) {
    this.vatIncluded = this.vatIncluded ?? false;
    this.acceptOnline = this.acceptOnline ?? false;
  }
}

export function getOfferIdentifier(offer: IOffer): number | undefined {
  return offer.id;
}
