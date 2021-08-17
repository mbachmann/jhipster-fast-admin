import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentText, getDocumentTextIdentifier } from '../document-text.model';

export type EntityResponseType = HttpResponse<IDocumentText>;
export type EntityArrayResponseType = HttpResponse<IDocumentText[]>;

@Injectable({ providedIn: 'root' })
export class DocumentTextService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentText: IDocumentText): Observable<EntityResponseType> {
    return this.http.post<IDocumentText>(this.resourceUrl, documentText, { observe: 'response' });
  }

  update(documentText: IDocumentText): Observable<EntityResponseType> {
    return this.http.put<IDocumentText>(`${this.resourceUrl}/${getDocumentTextIdentifier(documentText) as number}`, documentText, {
      observe: 'response',
    });
  }

  partialUpdate(documentText: IDocumentText): Observable<EntityResponseType> {
    return this.http.patch<IDocumentText>(`${this.resourceUrl}/${getDocumentTextIdentifier(documentText) as number}`, documentText, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentText>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentText[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentTextToCollectionIfMissing(
    documentTextCollection: IDocumentText[],
    ...documentTextsToCheck: (IDocumentText | null | undefined)[]
  ): IDocumentText[] {
    const documentTexts: IDocumentText[] = documentTextsToCheck.filter(isPresent);
    if (documentTexts.length > 0) {
      const documentTextCollectionIdentifiers = documentTextCollection.map(
        documentTextItem => getDocumentTextIdentifier(documentTextItem)!
      );
      const documentTextsToAdd = documentTexts.filter(documentTextItem => {
        const documentTextIdentifier = getDocumentTextIdentifier(documentTextItem);
        if (documentTextIdentifier == null || documentTextCollectionIdentifiers.includes(documentTextIdentifier)) {
          return false;
        }
        documentTextCollectionIdentifiers.push(documentTextIdentifier);
        return true;
      });
      return [...documentTextsToAdd, ...documentTextCollection];
    }
    return documentTextCollection;
  }
}
