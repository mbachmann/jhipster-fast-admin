import * as dayjs from 'dayjs';
import { ICustomFieldValueFa } from 'app/entities/custom-field-value-fa/custom-field-value-fa.model';
import { IDocumentFreeTextFa } from 'app/entities/document-free-text-fa/document-free-text-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ILayoutFa } from 'app/entities/layout-fa/layout-fa.model';
import { ISignatureFa } from 'app/entities/signature-fa/signature-fa.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { LetterStatus } from 'app/entities/enumerations/letter-status.model';

export interface IDocumentLetterFa {
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
  customFields?: ICustomFieldValueFa[] | null;
  freeTexts?: IDocumentFreeTextFa[] | null;
  contact?: IContactFa | null;
  contactAddress?: IContactAddressFa | null;
  contactPerson?: IContactPersonFa | null;
  contactPrePageAddress?: IContactAddressFa | null;
  layout?: ILayoutFa | null;
  layout?: ISignatureFa | null;
}

export class DocumentLetterFa implements IDocumentLetterFa {
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
    public customFields?: ICustomFieldValueFa[] | null,
    public freeTexts?: IDocumentFreeTextFa[] | null,
    public contact?: IContactFa | null,
    public contactAddress?: IContactAddressFa | null,
    public contactPerson?: IContactPersonFa | null,
    public contactPrePageAddress?: IContactAddressFa | null,
    public layout?: ILayoutFa | null,
    public layout?: ISignatureFa | null
  ) {}
}

export function getDocumentLetterFaIdentifier(documentLetter: IDocumentLetterFa): number | undefined {
  return documentLetter.id;
}
