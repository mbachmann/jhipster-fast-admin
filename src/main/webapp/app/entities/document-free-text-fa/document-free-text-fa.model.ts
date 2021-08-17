import { IDocumentLetterFa } from 'app/entities/document-letter-fa/document-letter-fa.model';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';

export interface IDocumentFreeTextFa {
  id?: number;
  text?: string | null;
  fontSize?: number | null;
  positionX?: number | null;
  positionY?: number | null;
  pageNo?: number | null;
  documentLetter?: IDocumentLetterFa | null;
  deliveryNote?: IDeliveryNoteFa | null;
  invoice?: IInvoiceFa | null;
  offer?: IOfferFa | null;
  orderConfirmation?: IOrderConfirmationFa | null;
}

export class DocumentFreeTextFa implements IDocumentFreeTextFa {
  constructor(
    public id?: number,
    public text?: string | null,
    public fontSize?: number | null,
    public positionX?: number | null,
    public positionY?: number | null,
    public pageNo?: number | null,
    public documentLetter?: IDocumentLetterFa | null,
    public deliveryNote?: IDeliveryNoteFa | null,
    public invoice?: IInvoiceFa | null,
    public offer?: IOfferFa | null,
    public orderConfirmation?: IOrderConfirmationFa | null
  ) {}
}

export function getDocumentFreeTextFaIdentifier(documentFreeText: IDocumentFreeTextFa): number | undefined {
  return documentFreeText.id;
}
