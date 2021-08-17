import * as dayjs from 'dayjs';
import { IDocumentFreeText } from 'app/entities/document-free-text/document-free-text.model';
import { IDescriptiveDocumentText } from 'app/entities/descriptive-document-text/descriptive-document-text.model';
import { IDocumentPosition } from 'app/entities/document-position/document-position.model';
import { IContact } from 'app/entities/contact/contact.model';
import { IContactAddress } from 'app/entities/contact-address/contact-address.model';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ILayout } from 'app/entities/layout/layout.model';
import { ISignature } from 'app/entities/signature/signature.model';
import { IBankAccount } from 'app/entities/bank-account/bank-account.model';
import { IIsr } from 'app/entities/isr/isr.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { IsrPosition } from 'app/entities/enumerations/isr-position.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';

export interface IInvoice {
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
  freeTexts?: IDocumentFreeText[] | null;
  texts?: IDescriptiveDocumentText[] | null;
  positions?: IDocumentPosition[] | null;
  contact?: IContact | null;
  contactAddress?: IContactAddress | null;
  contactPerson?: IContactPerson | null;
  contactPrePageAddress?: IContactAddress | null;
  layout?: ILayout | null;
  signature?: ISignature | null;
  bankAccount?: IBankAccount | null;
  isr?: IIsr | null;
}

export class Invoice implements IInvoice {
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
    public freeTexts?: IDocumentFreeText[] | null,
    public texts?: IDescriptiveDocumentText[] | null,
    public positions?: IDocumentPosition[] | null,
    public contact?: IContact | null,
    public contactAddress?: IContactAddress | null,
    public contactPerson?: IContactPerson | null,
    public contactPrePageAddress?: IContactAddress | null,
    public layout?: ILayout | null,
    public signature?: ISignature | null,
    public bankAccount?: IBankAccount | null,
    public isr?: IIsr | null
  ) {
    this.vatIncluded = this.vatIncluded ?? false;
    this.paymentLinkPaypal = this.paymentLinkPaypal ?? false;
    this.paymentLinkPostfinance = this.paymentLinkPostfinance ?? false;
    this.paymentLinkPayrexx = this.paymentLinkPayrexx ?? false;
    this.paymentLinkSmartcommerce = this.paymentLinkSmartcommerce ?? false;
  }
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
