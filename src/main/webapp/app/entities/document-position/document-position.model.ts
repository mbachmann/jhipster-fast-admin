import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IOffer } from 'app/entities/offer/offer.model';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { DocumentPositionType } from 'app/entities/enumerations/document-position-type.model';
import { CatalogScope } from 'app/entities/enumerations/catalog-scope.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';

export interface IDocumentPosition {
  id?: number;
  type?: DocumentPositionType | null;
  catalogType?: CatalogScope | null;
  number?: string | null;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  vat?: number | null;
  amount?: number | null;
  discountRate?: number | null;
  discountType?: DiscountType | null;
  total?: number | null;
  showOnlyTotal?: boolean | null;
  unit?: ICatalogUnit | null;
  deliveryNote?: IDeliveryNote | null;
  invoice?: IInvoice | null;
  offer?: IOffer | null;
  orderConfirmation?: IOrderConfirmation | null;
}

export class DocumentPosition implements IDocumentPosition {
  constructor(
    public id?: number,
    public type?: DocumentPositionType | null,
    public catalogType?: CatalogScope | null,
    public number?: string | null,
    public name?: string | null,
    public description?: string | null,
    public price?: number | null,
    public vat?: number | null,
    public amount?: number | null,
    public discountRate?: number | null,
    public discountType?: DiscountType | null,
    public total?: number | null,
    public showOnlyTotal?: boolean | null,
    public unit?: ICatalogUnit | null,
    public deliveryNote?: IDeliveryNote | null,
    public invoice?: IInvoice | null,
    public offer?: IOffer | null,
    public orderConfirmation?: IOrderConfirmation | null
  ) {
    this.showOnlyTotal = this.showOnlyTotal ?? false;
  }
}

export function getDocumentPositionIdentifier(documentPosition: IDocumentPosition): number | undefined {
  return documentPosition.id;
}
