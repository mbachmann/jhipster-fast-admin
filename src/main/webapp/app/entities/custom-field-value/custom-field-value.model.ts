import { ICustomField } from 'app/entities/custom-field/custom-field.model';
import { IContact } from 'app/entities/contact/contact.model';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { IProject } from 'app/entities/project/project.model';
import { ICatalogProduct } from 'app/entities/catalog-product/catalog-product.model';
import { ICatalogService } from 'app/entities/catalog-service/catalog-service.model';
import { IDocumentLetter } from 'app/entities/document-letter/document-letter.model';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';

export interface ICustomFieldValue {
  id?: number;
  key?: string;
  name?: string;
  value?: string | null;
  customField?: ICustomField | null;
  contact?: IContact | null;
  contactPerson?: IContactPerson | null;
  project?: IProject | null;
  catalogProduct?: ICatalogProduct | null;
  catalogService?: ICatalogService | null;
  documentLetter?: IDocumentLetter | null;
  deliveryNote?: IDeliveryNote | null;
}

export class CustomFieldValue implements ICustomFieldValue {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public value?: string | null,
    public customField?: ICustomField | null,
    public contact?: IContact | null,
    public contactPerson?: IContactPerson | null,
    public project?: IProject | null,
    public catalogProduct?: ICatalogProduct | null,
    public catalogService?: ICatalogService | null,
    public documentLetter?: IDocumentLetter | null,
    public deliveryNote?: IDeliveryNote | null
  ) {}
}

export function getCustomFieldValueIdentifier(customFieldValue: ICustomFieldValue): number | undefined {
  return customFieldValue.id;
}
