import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { DocumentTextUsage } from 'app/entities/enumerations/document-text-usage.model';
import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';
import { DocumentTextType } from 'app/entities/enumerations/document-text-type.model';
import { DocumentTextDocumentType } from 'app/entities/enumerations/document-text-document-type.model';

export interface IDocumentTextFa {
  id?: number;
  defaultText?: boolean | null;
  text?: string | null;
  language?: DocumentLanguage | null;
  usage?: DocumentTextUsage | null;
  status?: DocumentInvoiceTextStatus | null;
  type?: DocumentTextType | null;
  documentType?: DocumentTextDocumentType | null;
}

export class DocumentTextFa implements IDocumentTextFa {
  constructor(
    public id?: number,
    public defaultText?: boolean | null,
    public text?: string | null,
    public language?: DocumentLanguage | null,
    public usage?: DocumentTextUsage | null,
    public status?: DocumentInvoiceTextStatus | null,
    public type?: DocumentTextType | null,
    public documentType?: DocumentTextDocumentType | null
  ) {
    this.defaultText = this.defaultText ?? false;
  }
}

export function getDocumentTextFaIdentifier(documentText: IDocumentTextFa): number | undefined {
  return documentText.id;
}
