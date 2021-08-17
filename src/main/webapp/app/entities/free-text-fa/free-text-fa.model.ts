import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';

export interface IFreeTextFa {
  id?: number;
  text?: string | null;
  fontSize?: number | null;
  positionX?: number | null;
  positionY?: number | null;
  pageNo?: number | null;
  language?: DocumentLanguage | null;
}

export class FreeTextFa implements IFreeTextFa {
  constructor(
    public id?: number,
    public text?: string | null,
    public fontSize?: number | null,
    public positionX?: number | null,
    public positionY?: number | null,
    public pageNo?: number | null,
    public language?: DocumentLanguage | null
  ) {}
}

export function getFreeTextFaIdentifier(freeText: IFreeTextFa): number | undefined {
  return freeText.id;
}
