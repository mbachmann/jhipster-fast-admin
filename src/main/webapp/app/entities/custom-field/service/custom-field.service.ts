import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomField, getCustomFieldIdentifier } from '../custom-field.model';

export type EntityResponseType = HttpResponse<ICustomField>;
export type EntityArrayResponseType = HttpResponse<ICustomField[]>;

@Injectable({ providedIn: 'root' })
export class CustomFieldService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-fields');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customField: ICustomField): Observable<EntityResponseType> {
    return this.http.post<ICustomField>(this.resourceUrl, customField, { observe: 'response' });
  }

  update(customField: ICustomField): Observable<EntityResponseType> {
    return this.http.put<ICustomField>(`${this.resourceUrl}/${getCustomFieldIdentifier(customField) as number}`, customField, {
      observe: 'response',
    });
  }

  partialUpdate(customField: ICustomField): Observable<EntityResponseType> {
    return this.http.patch<ICustomField>(`${this.resourceUrl}/${getCustomFieldIdentifier(customField) as number}`, customField, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomField>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomField[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCustomFieldToCollectionIfMissing(
    customFieldCollection: ICustomField[],
    ...customFieldsToCheck: (ICustomField | null | undefined)[]
  ): ICustomField[] {
    const customFields: ICustomField[] = customFieldsToCheck.filter(isPresent);
    if (customFields.length > 0) {
      const customFieldCollectionIdentifiers = customFieldCollection.map(customFieldItem => getCustomFieldIdentifier(customFieldItem)!);
      const customFieldsToAdd = customFields.filter(customFieldItem => {
        const customFieldIdentifier = getCustomFieldIdentifier(customFieldItem);
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
