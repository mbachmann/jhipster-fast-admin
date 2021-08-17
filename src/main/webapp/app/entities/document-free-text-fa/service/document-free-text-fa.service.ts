import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentFreeTextFa, getDocumentFreeTextFaIdentifier } from '../document-free-text-fa.model';

export type EntityResponseType = HttpResponse<IDocumentFreeTextFa>;
export type EntityArrayResponseType = HttpResponse<IDocumentFreeTextFa[]>;

@Injectable({ providedIn: 'root' })
export class DocumentFreeTextFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-free-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentFreeText: IDocumentFreeTextFa): Observable<EntityResponseType> {
    return this.http.post<IDocumentFreeTextFa>(this.resourceUrl, documentFreeText, { observe: 'response' });
  }

  update(documentFreeText: IDocumentFreeTextFa): Observable<EntityResponseType> {
    return this.http.put<IDocumentFreeTextFa>(
      `${this.resourceUrl}/${getDocumentFreeTextFaIdentifier(documentFreeText) as number}`,
      documentFreeText,
      { observe: 'response' }
    );
  }

  partialUpdate(documentFreeText: IDocumentFreeTextFa): Observable<EntityResponseType> {
    return this.http.patch<IDocumentFreeTextFa>(
      `${this.resourceUrl}/${getDocumentFreeTextFaIdentifier(documentFreeText) as number}`,
      documentFreeText,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentFreeTextFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentFreeTextFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentFreeTextFaToCollectionIfMissing(
    documentFreeTextCollection: IDocumentFreeTextFa[],
    ...documentFreeTextsToCheck: (IDocumentFreeTextFa | null | undefined)[]
  ): IDocumentFreeTextFa[] {
    const documentFreeTexts: IDocumentFreeTextFa[] = documentFreeTextsToCheck.filter(isPresent);
    if (documentFreeTexts.length > 0) {
      const documentFreeTextCollectionIdentifiers = documentFreeTextCollection.map(
        documentFreeTextItem => getDocumentFreeTextFaIdentifier(documentFreeTextItem)!
      );
      const documentFreeTextsToAdd = documentFreeTexts.filter(documentFreeTextItem => {
        const documentFreeTextIdentifier = getDocumentFreeTextFaIdentifier(documentFreeTextItem);
        if (documentFreeTextIdentifier == null || documentFreeTextCollectionIdentifiers.includes(documentFreeTextIdentifier)) {
          return false;
        }
        documentFreeTextCollectionIdentifiers.push(documentFreeTextIdentifier);
        return true;
      });
      return [...documentFreeTextsToAdd, ...documentFreeTextCollection];
    }
    return documentFreeTextCollection;
  }
}
