import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentPosition, getDocumentPositionIdentifier } from '../document-position.model';

export type EntityResponseType = HttpResponse<IDocumentPosition>;
export type EntityArrayResponseType = HttpResponse<IDocumentPosition[]>;

@Injectable({ providedIn: 'root' })
export class DocumentPositionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-positions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentPosition: IDocumentPosition): Observable<EntityResponseType> {
    return this.http.post<IDocumentPosition>(this.resourceUrl, documentPosition, { observe: 'response' });
  }

  update(documentPosition: IDocumentPosition): Observable<EntityResponseType> {
    return this.http.put<IDocumentPosition>(
      `${this.resourceUrl}/${getDocumentPositionIdentifier(documentPosition) as number}`,
      documentPosition,
      { observe: 'response' }
    );
  }

  partialUpdate(documentPosition: IDocumentPosition): Observable<EntityResponseType> {
    return this.http.patch<IDocumentPosition>(
      `${this.resourceUrl}/${getDocumentPositionIdentifier(documentPosition) as number}`,
      documentPosition,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentPosition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentPositionToCollectionIfMissing(
    documentPositionCollection: IDocumentPosition[],
    ...documentPositionsToCheck: (IDocumentPosition | null | undefined)[]
  ): IDocumentPosition[] {
    const documentPositions: IDocumentPosition[] = documentPositionsToCheck.filter(isPresent);
    if (documentPositions.length > 0) {
      const documentPositionCollectionIdentifiers = documentPositionCollection.map(
        documentPositionItem => getDocumentPositionIdentifier(documentPositionItem)!
      );
      const documentPositionsToAdd = documentPositions.filter(documentPositionItem => {
        const documentPositionIdentifier = getDocumentPositionIdentifier(documentPositionItem);
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
