import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactPersonFa, getContactPersonFaIdentifier } from '../contact-person-fa.model';

export type EntityResponseType = HttpResponse<IContactPersonFa>;
export type EntityArrayResponseType = HttpResponse<IContactPersonFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactPersonFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-people');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactPerson: IContactPersonFa): Observable<EntityResponseType> {
    return this.http.post<IContactPersonFa>(this.resourceUrl, contactPerson, { observe: 'response' });
  }

  update(contactPerson: IContactPersonFa): Observable<EntityResponseType> {
    return this.http.put<IContactPersonFa>(`${this.resourceUrl}/${getContactPersonFaIdentifier(contactPerson) as number}`, contactPerson, {
      observe: 'response',
    });
  }

  partialUpdate(contactPerson: IContactPersonFa): Observable<EntityResponseType> {
    return this.http.patch<IContactPersonFa>(
      `${this.resourceUrl}/${getContactPersonFaIdentifier(contactPerson) as number}`,
      contactPerson,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactPersonFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactPersonFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactPersonFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContactPersonFaToCollectionIfMissing(
    contactPersonCollection: IContactPersonFa[],
    ...contactPeopleToCheck: (IContactPersonFa | null | undefined)[]
  ): IContactPersonFa[] {
    const contactPeople: IContactPersonFa[] = contactPeopleToCheck.filter(isPresent);
    if (contactPeople.length > 0) {
      const contactPersonCollectionIdentifiers = contactPersonCollection.map(
        contactPersonItem => getContactPersonFaIdentifier(contactPersonItem)!
      );
      const contactPeopleToAdd = contactPeople.filter(contactPersonItem => {
        const contactPersonIdentifier = getContactPersonFaIdentifier(contactPersonItem);
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
