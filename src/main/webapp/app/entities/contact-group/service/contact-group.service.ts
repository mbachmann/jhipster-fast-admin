import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactGroup, getContactGroupIdentifier } from '../contact-group.model';

export type EntityResponseType = HttpResponse<IContactGroup>;
export type EntityArrayResponseType = HttpResponse<IContactGroup[]>;

@Injectable({ providedIn: 'root' })
export class ContactGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactGroup: IContactGroup): Observable<EntityResponseType> {
    return this.http.post<IContactGroup>(this.resourceUrl, contactGroup, { observe: 'response' });
  }

  update(contactGroup: IContactGroup): Observable<EntityResponseType> {
    return this.http.put<IContactGroup>(`${this.resourceUrl}/${getContactGroupIdentifier(contactGroup) as number}`, contactGroup, {
      observe: 'response',
    });
  }

  partialUpdate(contactGroup: IContactGroup): Observable<EntityResponseType> {
    return this.http.patch<IContactGroup>(`${this.resourceUrl}/${getContactGroupIdentifier(contactGroup) as number}`, contactGroup, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactGroupToCollectionIfMissing(
    contactGroupCollection: IContactGroup[],
    ...contactGroupsToCheck: (IContactGroup | null | undefined)[]
  ): IContactGroup[] {
    const contactGroups: IContactGroup[] = contactGroupsToCheck.filter(isPresent);
    if (contactGroups.length > 0) {
      const contactGroupCollectionIdentifiers = contactGroupCollection.map(
        contactGroupItem => getContactGroupIdentifier(contactGroupItem)!
      );
      const contactGroupsToAdd = contactGroups.filter(contactGroupItem => {
        const contactGroupIdentifier = getContactGroupIdentifier(contactGroupItem);
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
