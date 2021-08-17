import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentPositionFa, getDocumentPositionFaIdentifier } from '../document-position-fa.model';

export type EntityResponseType = HttpResponse<IDocumentPositionFa>;
export type EntityArrayResponseType = HttpResponse<IDocumentPositionFa[]>;

@Injectable({ providedIn: 'root' })
export class DocumentPositionFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-positions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentPosition: IDocumentPositionFa): Observable<EntityResponseType> {
    return this.http.post<IDocumentPositionFa>(this.resourceUrl, documentPosition, { observe: 'response' });
  }

  update(documentPosition: IDocumentPositionFa): Observable<EntityResponseType> {
    return this.http.put<IDocumentPositionFa>(
      `${this.resourceUrl}/${getDocumentPositionFaIdentifier(documentPosition) as number}`,
      documentPosition,
      { observe: 'response' }
    );
  }

  partialUpdate(documentPosition: IDocumentPositionFa): Observable<EntityResponseType> {
    return this.http.patch<IDocumentPositionFa>(
      `${this.resourceUrl}/${getDocumentPositionFaIdentifier(documentPosition) as number}`,
      documentPosition,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentPositionFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentPositionFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentPositionFaToCollectionIfMissing(
    documentPositionCollection: IDocumentPositionFa[],
    ...documentPositionsToCheck: (IDocumentPositionFa | null | undefined)[]
  ): IDocumentPositionFa[] {
    const documentPositions: IDocumentPositionFa[] = documentPositionsToCheck.filter(isPresent);
    if (documentPositions.length > 0) {
      const documentPositionCollectionIdentifiers = documentPositionCollection.map(
        documentPositionItem => getDocumentPositionFaIdentifier(documentPositionItem)!
      );
      const documentPositionsToAdd = documentPositions.filter(documentPositionItem => {
        const documentPositionIdentifier = getDocumentPositionFaIdentifier(documentPositionItem);
        if (documentPositionIdentifier == null || documentPositionCollectionIdentifiers.includes(documentPositionIdentifier)) {
          return false;
        }
        documentPositionCollectionIdentifiers.push(documentPositionIdentifier);
        return true;
      });
      return [...documentPositionsToAdd, ...documentPositionCollection];
    }
    return documentPositionCollection;
  }
}
