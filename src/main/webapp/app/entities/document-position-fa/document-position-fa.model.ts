import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { DocumentPositionType } from 'app/entities/enumerations/document-position-type.model';
import { CatalogScope } from 'app/entities/enumerations/catalog-scope.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';

export interface IDocumentPositionFa {
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
  unit?: ICatalogUnitFa | null;
  deliveryNote?: IDeliveryNoteFa | null;
  invoice?: IInvoiceFa | null;
  offer?: IOfferFa | null;
  orderConfirmation?: IOrderConfirmationFa | null;
}

export class DocumentPositionFa implements IDocumentPositionFa {
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
    public unit?: ICatalogUnitFa | null,
    public deliveryNote?: IDeliveryNoteFa | null,
    public invoice?: IInvoiceFa | null,
    public offer?: IOfferFa | null,
    public orderConfirmation?: IOrderConfirmationFa | null
  ) {
    this.showOnlyTotal = this.showOnlyTotal ?? false;
  }
}

export function getDocumentPositionFaIdentifier(documentPosition: IDocumentPositionFa): number | undefined {
  return documentPosition.id;
}
