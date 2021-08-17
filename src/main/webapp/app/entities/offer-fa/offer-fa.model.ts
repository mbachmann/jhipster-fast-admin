import * as dayjs from 'dayjs';
import { IDocumentFreeTextFa } from 'app/entities/document-free-text-fa/document-free-text-fa.model';
import { IDescriptiveDocumentTextFa } from 'app/entities/descriptive-document-text-fa/descriptive-document-text-fa.model';
import { IDocumentPositionFa } from 'app/entities/document-position-fa/document-position-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ILayoutFa } from 'app/entities/layout-fa/layout-fa.model';
import { ISignatureFa } from 'app/entities/signature-fa/signature-fa.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { OfferAcceptOnlineStatus } from 'app/entities/enumerations/offer-accept-online-status.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { OfferStatus } from 'app/entities/enumerations/offer-status.model';

export interface IOfferFa {
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
  freeTexts?: IDocumentFreeTextFa[] | null;
  texts?: IDescriptiveDocumentTextFa[] | null;
  positions?: IDocumentPositionFa[] | null;
  contact?: IContactFa | null;
  contactAddress?: IContactAddressFa | null;
  contactPerson?: IContactPersonFa | null;
  contactPrePageAddress?: IContactAddressFa | null;
  layout?: ILayoutFa | null;
  layout?: ISignatureFa | null;
}

export class OfferFa implements IOfferFa {
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
    public freeTexts?: IDocumentFreeTextFa[] | null,
    public texts?: IDescriptiveDocumentTextFa[] | null,
    public positions?: IDocumentPositionFa[] | null,
    public contact?: IContactFa | null,
    public contactAddress?: IContactAddressFa | null,
    public contactPerson?: IContactPersonFa | null,
    public contactPrePageAddress?: IContactAddressFa | null,
    public layout?: ILayoutFa | null,
    public layout?: ISignatureFa | null
  ) {
    this.vatIncluded = this.vatIncluded ?? false;
    this.acceptOnline = this.acceptOnline ?? false;
  }
}

export function getOfferFaIdentifier(offer: IOfferFa): number | undefined {
  return offer.id;
}