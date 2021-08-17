import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentTextFa, getDocumentTextFaIdentifier } from '../document-text-fa.model';

export type EntityResponseType = HttpResponse<IDocumentTextFa>;
export type EntityArrayResponseType = HttpResponse<IDocumentTextFa[]>;

@Injectable({ providedIn: 'root' })
export class DocumentTextFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentText: IDocumentTextFa): Observable<EntityResponseType> {
    return this.http.post<IDocumentTextFa>(this.resourceUrl, documentText, { observe: 'response' });
  }

  update(documentText: IDocumentTextFa): Observable<EntityResponseType> {
    return this.http.put<IDocumentTextFa>(`${this.resourceUrl}/${getDocumentTextFaIdentifier(documentText) as number}`, documentText, {
      observe: 'response',
    });
  }

  partialUpdate(documentText: IDocumentTextFa): Observable<EntityResponseType> {
    return this.http.patch<IDocumentTextFa>(`${this.resourceUrl}/${getDocumentTextFaIdentifier(documentText) as number}`, documentText, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentTextFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentTextFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentTextFaToCollectionIfMissing(
    documentTextCollection: IDocumentTextFa[],
    ...documentTextsToCheck: (IDocumentTextFa | null | undefined)[]
  ): IDocumentTextFa[] {
    const documentTexts: IDocumentTextFa[] = documentTextsToCheck.filter(isPresent);
    if (documentTexts.length > 0) {
      const documentTextCollectionIdentifiers = documentTextCollection.map(
        documentTextItem => getDocumentTextFaIdentifier(documentTextItem)!
      );
      const documentTextsToAdd = documentTexts.filter(documentTextItem => {
        const documentTextIdentifier = getDocumentTextFaIdentifier(documentTextItem);
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
