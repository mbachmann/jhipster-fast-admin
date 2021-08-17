import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactGroupFa, getContactGroupFaIdentifier } from '../contact-group-fa.model';

export type EntityResponseType = HttpResponse<IContactGroupFa>;
export type EntityArrayResponseType = HttpResponse<IContactGroupFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactGroupFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactGroup: IContactGroupFa): Observable<EntityResponseType> {
    return this.http.post<IContactGroupFa>(this.resourceUrl, contactGroup, { observe: 'response' });
  }

  update(contactGroup: IContactGroupFa): Observable<EntityResponseType> {
    return this.http.put<IContactGroupFa>(`${this.resourceUrl}/${getContactGroupFaIdentifier(contactGroup) as number}`, contactGroup, {
      observe: 'response',
    });
  }

  partialUpdate(contactGroup: IContactGroupFa): Observable<EntityResponseType> {
    return this.http.patch<IContactGroupFa>(`${this.resourceUrl}/${getContactGroupFaIdentifier(contactGroup) as number}`, contactGroup, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactGroupFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactGroupFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactGroupFaToCollectionIfMissing(
    contactGroupCollection: IContactGroupFa[],
    ...contactGroupsToCheck: (IContactGroupFa | null | undefined)[]
  ): IContactGroupFa[] {
    const contactGroups: IContactGroupFa[] = contactGroupsToCheck.filter(isPresent);
    if (contactGroups.length > 0) {
      const contactGroupCollectionIdentifiers = contactGroupCollection.map(
        contactGroupItem => getContactGroupFaIdentifier(contactGroupItem)!
      );
      const contactGroupsToAdd = contactGroups.filter(contactGroupItem => {
        const contactGroupIdentifier = getContactGroupFaIdentifier(contactGroupItem);
        if (contactGroupIdentifier == null || contactGroupCollectionIdentifiers.includes(contactGroupIdentifier)) {
          return false;
        }
        contactGroupCollectionIdentifiers.push(contactGroupIdentifier);
        return true;
      });
      return [...contactGroupsToAdd, ...contactGroupCollection];
    }
    return contactGroupCollection;
  }
}
