import * as dayjs from 'dayjs';
import { IDocumentFreeTextFa } from 'app/entities/document-free-text-fa/document-free-text-fa.model';
import { IDescriptiveDocumentTextFa } from 'app/entities/descriptive-document-text-fa/descriptive-document-text-fa.model';
import { IDocumentPositionFa } from 'app/entities/document-position-fa/document-position-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ILayoutFa } from 'app/entities/layout-fa/layout-fa.model';
import { ISignatureFa } from 'app/entities/signature-fa/signature-fa.model';
import { IBankAccountFa } from 'app/entities/bank-account-fa/bank-account-fa.model';
import { IIsrFa } from 'app/entities/isr-fa/isr-fa.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { IsrPosition } from 'app/entities/enumerations/isr-position.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';

export interface IInvoiceFa {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
  contactName?: string | null;
  date?: dayjs.Dayjs | null;
  due?: dayjs.Dayjs | null;
  periodFrom?: dayjs.Dayjs | null;
  periodTo?: dayjs.Dayjs | null;
  periodText?: string | null;
  currency?: Currency | null;
  total?: number | null;
  vatIncluded?: boolean | null;
  discountRate?: number | null;
  discountType?: DiscountType | null;
  cashDiscountRate?: number | null;
  cashDiscountDate?: dayjs.Dayjs | null;
  totalPaid?: number | null;
  paidDate?: string | null;
  isrPosition?: IsrPosition | null;
  isrReferenceNumber?: string | null;
  paymentLinkPaypal?: boolean | null;
  paymentLinkPaypalUrl?: string | null;
  paymentLinkPostfinance?: boolean | null;
  paymentLinkPostfinanceUrl?: string | null;
  paymentLinkPayrexx?: boolean | null;
  paymentLinkPayrexxUrl?: string | null;
  paymentLinkSmartcommerce?: boolean | null;
  paymentLinkSmartcommerceUrl?: string | null;
  language?: DocumentLanguage | null;
  pageAmount?: number | null;
  notes?: string | null;
  status?: InvoiceStatus | null;
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
  bankAccount?: IBankAccountFa | null;
  isr?: IIsrFa | null;
}

export class InvoiceFa implements IInvoiceFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
    public contactName?: string | null,
    public date?: dayjs.Dayjs | null,
    public due?: dayjs.Dayjs | null,
    public periodFrom?: dayjs.Dayjs | null,
    public periodTo?: dayjs.Dayjs | null,
    public periodText?: string | null,
    public currency?: Currency | null,
    public total?: number | null,
    public vatIncluded?: boolean | null,
    public discountRate?: number | null,
    public discountType?: DiscountType | null,
    public cashDiscountRate?: number | null,
    public cashDiscountDate?: dayjs.Dayjs | null,
    public totalPaid?: number | null,
    public paidDate?: string | null,
    public isrPosition?: IsrPosition | null,
    public isrReferenceNumber?: string | null,
    public paymentLinkPaypal?: boolean | null,
    public paymentLinkPaypalUrl?: string | null,
    public paymentLinkPostfinance?: boolean | null,
    public paymentLinkPostfinanceUrl?: string | null,
    public paymentLinkPayrexx?: boolean | null,
    public paymentLinkPayrexxUrl?: string | null,
    public paymentLinkSmartcommerce?: boolean | null,
    public paymentLinkSmartcommerceUrl?: string | null,
    public language?: DocumentLanguage | null,
    public pageAmount?: number | null,
    public notes?: string | null,
    public status?: InvoiceStatus | null,
    public created?: dayjs.Dayjs | null,
    public freeTexts?: IDocumentFreeTextFa[] | null,
    public texts?: IDescriptiveDocumentTextFa[] | null,
    public positions?: IDocumentPositionFa[] | null,
    public contact?: IContactFa | null,
    public contactAddress?: IContactAddressFa | null,
    public contactPerson?: IContactPersonFa | null,
    public contactPrePageAddress?: IContactAddressFa | null,
    public layout?: ILayoutFa | null,
    public layout?: ISignatureFa | null,
    public bankAccount?: IBankAccountFa | null,
    public isr?: IIsrFa | null
  ) {
    this.vatIncluded = this.vatIncluded ?? false;
    this.paymentLinkPaypal = this.paymentLinkPaypal ?? false;
    this.paymentLinkPostfinance = this.paymentLinkPostfinance ?? false;
    this.paymentLinkPayrexx = this.paymentLinkPayrexx ?? false;
    this.paymentLinkSmartcommerce = this.paymentLinkSmartcommerce ?? false;
  }
}

export function getInvoiceFaIdentifier(invoice: IInvoiceFa): number | undefined {
  return invoice.id;
}
