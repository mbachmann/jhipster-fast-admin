import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';

export interface IDescriptiveDocumentTextFa {
  id?: number;
  title?: string | null;
  introduction?: string | null;
  conditions?: string | null;
  status?: DocumentInvoiceTextStatus | null;
  deliveryNote?: IDeliveryNoteFa | null;
  invoice?: IInvoiceFa | null;
  offer?: IOfferFa | null;
  orderConfirmation?: IOrderConfirmationFa | null;
}

export class DescriptiveDocumentTextFa implements IDescriptiveDocumentTextFa {
  constructor(
    public id?: number,
    public title?: string | null,
    public introduction?: string | null,
    public conditions?: string | null,
    public status?: DocumentInvoiceTextStatus | null,
    public deliveryNote?: IDeliveryNoteFa | null,
    public invoice?: IInvoiceFa | null,
    public offer?: IOfferFa | null,
    public orderConfirmation?: IOrderConfirmationFa | null
  ) {}
}

export function getDescriptiveDocumentTextFaIdentifier(descriptiveDocumentText: IDescriptiveDocumentTextFa): number | undefined {
  return descriptiveDocumentText.id;
}
