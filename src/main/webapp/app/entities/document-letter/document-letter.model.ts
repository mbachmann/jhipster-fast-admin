import * as dayjs from 'dayjs';
import { ICustomFieldValue } from 'app/entities/custom-field-value/custom-field-value.model';
import { IDocumentFreeText } from 'app/entities/document-free-text/document-free-text.model';
import { IContact } from 'app/entities/contact/contact.model';
import { IContactAddress } from 'app/entities/contact-address/contact-address.model';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ILayout } from 'app/entities/layout/layout.model';
import { ISignature } from 'app/entities/signature/signature.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { LetterStatus } from 'app/entities/enumerations/letter-status.model';

export interface IDocumentLetter {
  id?: number;
  remoteId?: number | null;
  contactName?: string | null;
  date?: dayjs.Dayjs | null;
  title?: string | null;
  content?: string | null;
  language?: DocumentLanguage | null;
  pageAmount?: number | null;
  notes?: string | null;
  status?: LetterStatus | null;
  created?: dayjs.Dayjs | null;
  customFields?: ICustomFieldValue[] | null;
  freeTexts?: IDocumentFreeText[] | null;
  contact?: IContact | null;
  contactAddress?: IContactAddress | null;
  contactPerson?: IContactPerson | null;
  contactPrePageAddress?: IContactAddress | null;
  layout?: ILayout | null;
  signature?: ISignature | null;
}

export class DocumentLetter implements IDocumentLetter {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public contactName?: string | null,
    public date?: dayjs.Dayjs | null,
    public title?: string | null,
    public content?: string | null,
    public language?: DocumentLanguage | null,
    public pageAmount?: number | null,
    public notes?: string | null,
    public status?: LetterStatus | null,
    public created?: dayjs.Dayjs | null,
    public customFields?: ICustomFieldValue[] | null,
    public freeTexts?: IDocumentFreeText[] | null,
    public contact?: IContact | null,
    public contactAddress?: IContactAddress | null,
    public contactPerson?: IContactPerson | null,
    public contactPrePageAddress?: IContactAddress | null,
    public layout?: ILayout | null,
    public signature?: ISignature | null
  ) {}
}

export function getDocumentLetterIdentifier(documentLetter: IDocumentLetter): number | undefined {
  return documentLetter.id;
}
