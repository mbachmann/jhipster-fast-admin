import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IOffer } from 'app/entities/offer/offer.model';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';

export interface IDescriptiveDocumentText {
  id?: number;
  title?: string | null;
  introduction?: string | null;
  conditions?: string | null;
  status?: DocumentInvoiceTextStatus | null;
  deliveryNote?: IDeliveryNote | null;
  invoice?: IInvoice | null;
  offer?: IOffer | null;
  orderConfirmation?: IOrderConfirmation | null;
}

export class DescriptiveDocumentText implements IDescriptiveDocumentText {
  constructor(
    public id?: number,
    public title?: string | null,
    public introduction?: string | null,
    public conditions?: string | null,
    public status?: DocumentInvoiceTextStatus | null,
    public deliveryNote?: IDeliveryNote | null,
    public invoice?: IInvoice | null,
    public offer?: IOffer | null,
    public orderConfirmation?: IOrderConfirmation | null
  ) {}
}

export function getDescriptiveDocumentTextIdentifier(descriptiveDocumentText: IDescriptiveDocumentText): number | undefined {
  return descriptiveDocumentText.id;
}
