import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomFieldValue, getCustomFieldValueIdentifier } from '../custom-field-value.model';

export type EntityResponseType = HttpResponse<ICustomFieldValue>;
export type EntityArrayResponseType = HttpResponse<ICustomFieldValue[]>;

@Injectable({ providedIn: 'root' })
export class CustomFieldValueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-field-values');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customFieldValue: ICustomFieldValue): Observable<EntityResponseType> {
    return this.http.post<ICustomFieldValue>(this.resourceUrl, customFieldValue, { observe: 'response' });
  }

  update(customFieldValue: ICustomFieldValue): Observable<EntityResponseType> {
    return this.http.put<ICustomFieldValue>(
      `${this.resourceUrl}/${getCustomFieldValueIdentifier(customFieldValue) as number}`,
      customFieldValue,
      { observe: 'response' }
    );
  }

  partialUpdate(customFieldValue: ICustomFieldValue): Observable<EntityResponseType> {
    return this.http.patch<ICustomFieldValue>(
      `${this.resourceUrl}/${getCustomFieldValueIdentifier(customFieldValue) as number}`,
      customFieldValue,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomFieldValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomFieldValue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCustomFieldValueToCollectionIfMissing(
    customFieldValueCollection: ICustomFieldValue[],
    ...customFieldValuesToCheck: (ICustomFieldValue | null | undefined)[]
  ): ICustomFieldValue[] {
    const customFieldValues: ICustomFieldValue[] = customFieldValuesToCheck.filter(isPresent);
    if (customFieldValues.length > 0) {
      const customFieldValueCollectionIdentifiers = customFieldValueCollection.map(
        customFieldValueItem => getCustomFieldValueIdentifier(customFieldValueItem)!
      );
      const customFieldValuesToAdd = customFieldValues.filter(customFieldValueItem => {
        const customFieldValueIdentifier = getCustomFieldValueIdentifier(customFieldValueItem);
        if (customFieldValueIdentifier == null || customFieldValueCollectionIdentifiers.includes(customFieldValueIdentifier)) {
          return false;
        }
        customFieldValueCollectionIdentifiers.push(customFieldValueIdentifier);
        return true;
      });
      return [...customFieldValuesToAdd, ...customFieldValueCollection];
    }
    return customFieldValueCollection;
  }
}
