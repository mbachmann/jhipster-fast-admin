import { IDocumentLetter } from 'app/entities/document-letter/document-letter.model';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IOffer } from 'app/entities/offer/offer.model';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';

export interface IDocumentFreeText {
  id?: number;
  text?: string | null;
  fontSize?: number | null;
  positionX?: number | null;
  positionY?: number | null;
  pageNo?: number | null;
  documentLetter?: IDocumentLetter | null;
  deliveryNote?: IDeliveryNote | null;
  invoice?: IInvoice | null;
  offer?: IOffer | null;
  orderConfirmation?: IOrderConfirmation | null;
}

export class DocumentFreeText implements IDocumentFreeText {
  constructor(
    public id?: number,
    public text?: string | null,
    public fontSize?: number | null,
    public positionX?: number | null,
    public positionY?: number | null,
    public pageNo?: number | null,
    public documentLetter?: IDocumentLetter | null,
    public deliveryNote?: IDeliveryNote | null,
    public invoice?: IInvoice | null,
    public offer?: IOffer | null,
    public orderConfirmation?: IOrderConfirmation | null
  ) {}
}

export function getDocumentFreeTextIdentifier(documentFreeText: IDocumentFreeText): number | undefined {
  return documentFreeText.id;
}
