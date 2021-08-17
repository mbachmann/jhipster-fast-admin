import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDescriptiveDocumentTextFa, getDescriptiveDocumentTextFaIdentifier } from '../descriptive-document-text-fa.model';

export type EntityResponseType = HttpResponse<IDescriptiveDocumentTextFa>;
export type EntityArrayResponseType = HttpResponse<IDescriptiveDocumentTextFa[]>;

@Injectable({ providedIn: 'root' })
export class DescriptiveDocumentTextFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/descriptive-document-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(descriptiveDocumentText: IDescriptiveDocumentTextFa): Observable<EntityResponseType> {
    return this.http.post<IDescriptiveDocumentTextFa>(this.resourceUrl, descriptiveDocumentText, { observe: 'response' });
  }

  update(descriptiveDocumentText: IDescriptiveDocumentTextFa): Observable<EntityResponseType> {
    return this.http.put<IDescriptiveDocumentTextFa>(
      `${this.resourceUrl}/${getDescriptiveDocumentTextFaIdentifier(descriptiveDocumentText) as number}`,
      descriptiveDocumentText,
      { observe: 'response' }
    );
  }

  partialUpdate(descriptiveDocumentText: IDescriptiveDocumentTextFa): Observable<EntityResponseType> {
    return this.http.patch<IDescriptiveDocumentTextFa>(
      `${this.resourceUrl}/${getDescriptiveDocumentTextFaIdentifier(descriptiveDocumentText) as number}`,
      descriptiveDocumentText,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDescriptiveDocumentTextFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDescriptiveDocumentTextFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDescriptiveDocumentTextFaToCollectionIfMissing(
    descriptiveDocumentTextCollection: IDescriptiveDocumentTextFa[],
    ...descriptiveDocumentTextsToCheck: (IDescriptiveDocumentTextFa | null | undefined)[]
  ): IDescriptiveDocumentTextFa[] {
    const descriptiveDocumentTexts: IDescriptiveDocumentTextFa[] = descriptiveDocumentTextsToCheck.filter(isPresent);
    if (descriptiveDocumentTexts.length > 0) {
      const descriptiveDocumentTextCollectionIdentifiers = descriptiveDocumentTextCollection.map(
        descriptiveDocumentTextItem => getDescriptiveDocumentTextFaIdentifier(descriptiveDocumentTextItem)!
      );
      const descriptiveDocumentTextsToAdd = descriptiveDocumentTexts.filter(descriptiveDocumentTextItem => {
        const descriptiveDocumentTextIdentifier = getDescriptiveDocumentTextFaIdentifier(descriptiveDocumentTextItem);
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
