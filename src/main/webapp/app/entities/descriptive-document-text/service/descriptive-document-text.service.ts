import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDescriptiveDocumentText, getDescriptiveDocumentTextIdentifier } from '../descriptive-document-text.model';

export type EntityResponseType = HttpResponse<IDescriptiveDocumentText>;
export type EntityArrayResponseType = HttpResponse<IDescriptiveDocumentText[]>;

@Injectable({ providedIn: 'root' })
export class DescriptiveDocumentTextService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/descriptive-document-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(descriptiveDocumentText: IDescriptiveDocumentText): Observable<EntityResponseType> {
    return this.http.post<IDescriptiveDocumentText>(this.resourceUrl, descriptiveDocumentText, { observe: 'response' });
  }

  update(descriptiveDocumentText: IDescriptiveDocumentText): Observable<EntityResponseType> {
    return this.http.put<IDescriptiveDocumentText>(
      `${this.resourceUrl}/${getDescriptiveDocumentTextIdentifier(descriptiveDocumentText) as number}`,
      descriptiveDocumentText,
      { observe: 'response' }
    );
  }

  partialUpdate(descriptiveDocumentText: IDescriptiveDocumentText): Observable<EntityResponseType> {
    return this.http.patch<IDescriptiveDocumentText>(
      `${this.resourceUrl}/${getDescriptiveDocumentTextIdentifier(descriptiveDocumentText) as number}`,
      descriptiveDocumentText,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDescriptiveDocumentText>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDescriptiveDocumentText[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDescriptiveDocumentTextToCollectionIfMissing(
    descriptiveDocumentTextCollection: IDescriptiveDocumentText[],
    ...descriptiveDocumentTextsToCheck: (IDescriptiveDocumentText | null | undefined)[]
  ): IDescriptiveDocumentText[] {
    const descriptiveDocumentTexts: IDescriptiveDocumentText[] = descriptiveDocumentTextsToCheck.filter(isPresent);
    if (descriptiveDocumentTexts.length > 0) {
      const descriptiveDocumentTextCollectionIdentifiers = descriptiveDocumentTextCollection.map(
        descriptiveDocumentTextItem => getDescriptiveDocumentTextIdentifier(descriptiveDocumentTextItem)!
      );
      const descriptiveDocumentTextsToAdd = descriptiveDocumentTexts.filter(descriptiveDocumentTextItem => {
        const descriptiveDocumentTextIdentifier = getDescriptiveDocumentTextIdentifier(descriptiveDocumentTextItem);
        if (
          descriptiveDocumentTextIdentifier == null ||
          descriptiveDocumentTextCollectionIdentifiers.includes(descriptiveDocumentTextIdentifier)
        ) {
          return false;
        }
        descriptiveDocumentTextCollectionIdentifiers.push(descriptiveDocumentTextIdentifier);
        return true;
      });
      return [...descriptiveDocumentTextsToAdd, ...descriptiveDocumentTextCollection];
    }
    return descriptiveDocumentTextCollection;
  }
}
