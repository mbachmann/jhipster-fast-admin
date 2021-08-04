import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ICustomFieldMySuffix, getCustomFieldMySuffixIdentifier } from '../custom-field-my-suffix.model';

export type EntityResponseType = HttpResponse<ICustomFieldMySuffix>;
export type EntityArrayResponseType = HttpResponse<ICustomFieldMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class CustomFieldMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-fields');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/custom-fields');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customField: ICustomFieldMySuffix): Observable<EntityResponseType> {
    return this.http.post<ICustomFieldMySuffix>(this.resourceUrl, customField, { observe: 'response' });
  }

  update(customField: ICustomFieldMySuffix): Observable<EntityResponseType> {
    return this.http.put<ICustomFieldMySuffix>(
      `${this.resourceUrl}/${getCustomFieldMySuffixIdentifier(customField) as number}`,
      customField,
      { observe: 'response' }
    );
  }

  partialUpdate(customField: ICustomFieldMySuffix): Observable<EntityResponseType> {
    return this.http.patch<ICustomFieldMySuffix>(
      `${this.resourceUrl}/${getCustomFieldMySuffixIdentifier(customField) as number}`,
      customField,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomFieldMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomFieldMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomFieldMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCustomFieldMySuffixToCollectionIfMissing(
    customFieldCollection: ICustomFieldMySuffix[],
    ...customFieldsToCheck: (ICustomFieldMySuffix | null | undefined)[]
  ): ICustomFieldMySuffix[] {
    const customFields: ICustomFieldMySuffix[] = customFieldsToCheck.filter(isPresent);
    if (customFields.length > 0) {
      const customFieldCollectionIdentifiers = customFieldCollection.map(
        customFieldItem => getCustomFieldMySuffixIdentifier(customFieldItem)!
      );
      const customFieldsToAdd = customFields.filter(customFieldItem => {
        const customFieldIdentifier = getCustomFieldMySuffixIdentifier(customFieldItem);
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
