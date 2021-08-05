import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactPersonMySuffix, getContactPersonMySuffixIdentifier } from '../contact-person-my-suffix.model';

export type EntityResponseType = HttpResponse<IContactPersonMySuffix>;
export type EntityArrayResponseType = HttpResponse<IContactPersonMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ContactPersonMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-people');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactPerson: IContactPersonMySuffix): Observable<EntityResponseType> {
    return this.http.post<IContactPersonMySuffix>(this.resourceUrl, contactPerson, { observe: 'response' });
  }

  update(contactPerson: IContactPersonMySuffix): Observable<EntityResponseType> {
    return this.http.put<IContactPersonMySuffix>(
      `${this.resourceUrl}/${getContactPersonMySuffixIdentifier(contactPerson) as number}`,
      contactPerson,
      { observe: 'response' }
    );
  }

  partialUpdate(contactPerson: IContactPersonMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IContactPersonMySuffix>(
      `${this.resourceUrl}/${getContactPersonMySuffixIdentifier(contactPerson) as number}`,
      contactPerson,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactPersonMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactPersonMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactPersonMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContactPersonMySuffixToCollectionIfMissing(
    contactPersonCollection: IContactPersonMySuffix[],
    ...contactPeopleToCheck: (IContactPersonMySuffix | null | undefined)[]
  ): IContactPersonMySuffix[] {
    const contactPeople: IContactPersonMySuffix[] = contactPeopleToCheck.filter(isPresent);
    if (contactPeople.length > 0) {
      const contactPersonCollectionIdentifiers = contactPersonCollection.map(
        contactPersonItem => getContactPersonMySuffixIdentifier(contactPersonItem)!
      );
      const contactPeopleToAdd = contactPeople.filter(contactPersonItem => {
        const contactPersonIdentifier = getContactPersonMySuffixIdentifier(contactPersonItem);
        if (contactPersonIdentifier == null || contactPersonCollectionIdentifiers.includes(contactPersonIdentifier)) {
          return false;
        }
        contactPersonCollectionIdentifiers.push(contactPersonIdentifier);
        return true;
      });
      return [...contactPeopleToAdd, ...contactPersonCollection];
    }
    return contactPersonCollection;
  }
}
