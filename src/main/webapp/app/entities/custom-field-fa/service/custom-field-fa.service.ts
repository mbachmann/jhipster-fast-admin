import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ICustomFieldFa, getCustomFieldFaIdentifier } from '../custom-field-fa.model';

export type EntityResponseType = HttpResponse<ICustomFieldFa>;
export type EntityArrayResponseType = HttpResponse<ICustomFieldFa[]>;

@Injectable({ providedIn: 'root' })
export class CustomFieldFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-fields');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/custom-fields');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customField: ICustomFieldFa): Observable<EntityResponseType> {
    return this.http.post<ICustomFieldFa>(this.resourceUrl, customField, { observe: 'response' });
  }

  update(customField: ICustomFieldFa): Observable<EntityResponseType> {
    return this.http.put<ICustomFieldFa>(`${this.resourceUrl}/${getCustomFieldFaIdentifier(customField) as number}`, customField, {
      observe: 'response',
    });
  }

  partialUpdate(customField: ICustomFieldFa): Observable<EntityResponseType> {
    return this.http.patch<ICustomFieldFa>(`${this.resourceUrl}/${getCustomFieldFaIdentifier(customField) as number}`, customField, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomFieldFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomFieldFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomFieldFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCustomFieldFaToCollectionIfMissing(
    customFieldCollection: ICustomFieldFa[],
    ...customFieldsToCheck: (ICustomFieldFa | null | undefined)[]
  ): ICustomFieldFa[] {
    const customFields: ICustomFieldFa[] = customFieldsToCheck.filter(isPresent);
    if (customFields.length > 0) {
      const customFieldCollectionIdentifiers = customFieldCollection.map(customFieldItem => getCustomFieldFaIdentifier(customFieldItem)!);
      const customFieldsToAdd = customFields.filter(customFieldItem => {
        const customFieldIdentifier = getCustomFieldFaIdentifier(customFieldItem);
        if (customFieldIdentifier == null || customFieldCollectionIdentifiers.includes(customFieldIdentifier)) {
          return false;
        }
        customFieldCollectionIdentifiers.push(customFieldIdentifier);
        return true;
      });
      return [...customFieldsToAdd, ...customFieldCollection];
    }
    return customFieldCollection;
  }
}
