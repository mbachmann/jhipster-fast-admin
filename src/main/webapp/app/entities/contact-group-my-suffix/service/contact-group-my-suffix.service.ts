import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactGroupMySuffix, getContactGroupMySuffixIdentifier } from '../contact-group-my-suffix.model';

export type EntityResponseType = HttpResponse<IContactGroupMySuffix>;
export type EntityArrayResponseType = HttpResponse<IContactGroupMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ContactGroupMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-groups');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactGroup: IContactGroupMySuffix): Observable<EntityResponseType> {
    return this.http.post<IContactGroupMySuffix>(this.resourceUrl, contactGroup, { observe: 'response' });
  }

  update(contactGroup: IContactGroupMySuffix): Observable<EntityResponseType> {
    return this.http.put<IContactGroupMySuffix>(
      `${this.resourceUrl}/${getContactGroupMySuffixIdentifier(contactGroup) as number}`,
      contactGroup,
      { observe: 'response' }
    );
  }

  partialUpdate(contactGroup: IContactGroupMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IContactGroupMySuffix>(
      `${this.resourceUrl}/${getContactGroupMySuffixIdentifier(contactGroup) as number}`,
      contactGroup,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactGroupMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactGroupMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactGroupMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContactGroupMySuffixToCollectionIfMissing(
    contactGroupCollection: IContactGroupMySuffix[],
    ...contactGroupsToCheck: (IContactGroupMySuffix | null | undefined)[]
  ): IContactGroupMySuffix[] {
    const contactGroups: IContactGroupMySuffix[] = contactGroupsToCheck.filter(isPresent);
    if (contactGroups.length > 0) {
      const contactGroupCollectionIdentifiers = contactGroupCollection.map(
        contactGroupItem => getContactGroupMySuffixIdentifier(contactGroupItem)!
      );
      const contactGroupsToAdd = contactGroups.filter(contactGroupItem => {
        const contactGroupIdentifier = getContactGroupMySuffixIdentifier(contactGroupItem);
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
