import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IContactFa, getContactFaIdentifier } from '../contact-fa.model';

export type EntityResponseType = HttpResponse<IContactFa>;
export type EntityArrayResponseType = HttpResponse<IContactFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contacts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contacts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contact: IContactFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contact);
    return this.http
      .post<IContactFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contact: IContactFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contact);
    return this.http
      .put<IContactFa>(`${this.resourceUrl}/${getContactFaIdentifier(contact) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contact: IContactFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contact);
    return this.http
      .patch<IContactFa>(`${this.resourceUrl}/${getContactFaIdentifier(contact) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addContactFaToCollectionIfMissing(contactCollection: IContactFa[], ...contactsToCheck: (IContactFa | null | undefined)[]): IContactFa[] {
    const contacts: IContactFa[] = contactsToCheck.filter(isPresent);
    if (contacts.length > 0) {
      const contactCollectionIdentifiers = contactCollection.map(contactItem => getContactFaIdentifier(contactItem)!);
      const contactsToAdd = contacts.filter(contactItem => {
        const contactIdentifier = getContactFaIdentifier(contactItem);
        if (contactIdentifier == null || contactCollectionIdentifiers.includes(contactIdentifier)) {
          return false;
        }
        contactCollectionIdentifiers.push(contactIdentifier);
        return true;
      });
      return [...contactsToAdd, ...contactCollection];
    }
    return contactCollection;
  }

  protected convertDateFromClient(contact: IContactFa): IContactFa {
    return Object.assign({}, contact, {
      created: contact.created?.isValid() ? contact.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contact: IContactFa) => {
        contact.created = contact.created ? dayjs(contact.created) : undefined;
      });
    }
    return res;
  }
}
