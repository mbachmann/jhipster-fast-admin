import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentFreeText, getDocumentFreeTextIdentifier } from '../document-free-text.model';

export type EntityResponseType = HttpResponse<IDocumentFreeText>;
export type EntityArrayResponseType = HttpResponse<IDocumentFreeText[]>;

@Injectable({ providedIn: 'root' })
export class DocumentFreeTextService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-free-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentFreeText: IDocumentFreeText): Observable<EntityResponseType> {
    return this.http.post<IDocumentFreeText>(this.resourceUrl, documentFreeText, { observe: 'response' });
  }

  update(documentFreeText: IDocumentFreeText): Observable<EntityResponseType> {
    return this.http.put<IDocumentFreeText>(
      `${this.resourceUrl}/${getDocumentFreeTextIdentifier(documentFreeText) as number}`,
      documentFreeText,
      { observe: 'response' }
    );
  }

  partialUpdate(documentFreeText: IDocumentFreeText): Observable<EntityResponseType> {
    return this.http.patch<IDocumentFreeText>(
      `${this.resourceUrl}/${getDocumentFreeTextIdentifier(documentFreeText) as number}`,
      documentFreeText,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentFreeText>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentFreeText[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentFreeTextToCollectionIfMissing(
    documentFreeTextCollection: IDocumentFreeText[],
    ...documentFreeTextsToCheck: (IDocumentFreeText | null | undefined)[]
  ): IDocumentFreeText[] {
    const documentFreeTexts: IDocumentFreeText[] = documentFreeTextsToCheck.filter(isPresent);
    if (documentFreeTexts.length > 0) {
      const documentFreeTextCollectionIdentifiers = documentFreeTextCollection.map(
        documentFreeTextItem => getDocumentFreeTextIdentifier(documentFreeTextItem)!
      );
      const documentFreeTextsToAdd = documentFreeTexts.filter(documentFreeTextItem => {
        const documentFreeTextIdentifier = getDocumentFreeTextIdentifier(documentFreeTextItem);
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
