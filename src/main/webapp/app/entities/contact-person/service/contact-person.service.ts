import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactPerson, getContactPersonIdentifier } from '../contact-person.model';

export type EntityResponseType = HttpResponse<IContactPerson>;
export type EntityArrayResponseType = HttpResponse<IContactPerson[]>;

@Injectable({ providedIn: 'root' })
export class ContactPersonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactPerson: IContactPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactPerson);
    return this.http
      .post<IContactPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactPerson: IContactPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactPerson);
    return this.http
      .put<IContactPerson>(`${this.resourceUrl}/${getContactPersonIdentifier(contactPerson) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contactPerson: IContactPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactPerson);
    return this.http
      .patch<IContactPerson>(`${this.resourceUrl}/${getContactPersonIdentifier(contactPerson) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactPerson[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactPersonToCollectionIfMissing(
    contactPersonCollection: IContactPerson[],
    ...contactPeopleToCheck: (IContactPerson | null | undefined)[]
  ): IContactPerson[] {
    const contactPeople: IContactPerson[] = contactPeopleToCheck.filter(isPresent);
    if (contactPeople.length > 0) {
      const contactPersonCollectionIdentifiers = contactPersonCollection.map(
        contactPersonItem => getContactPersonIdentifier(contactPersonItem)!
      );
      const contactPeopleToAdd = contactPeople.filter(contactPersonItem => {
        const contactPersonIdentifier = getContactPersonIdentifier(contactPersonItem);
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

  protected convertDateFromClient(contactPerson: IContactPerson): IContactPerson {
    return Object.assign({}, contactPerson, {
      birthDate: contactPerson.birthDate?.isValid() ? contactPerson.birthDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate ? dayjs(res.body.birthDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contactPerson: IContactPerson) => {
        contactPerson.birthDate = contactPerson.birthDate ? dayjs(contactPerson.birthDate) : undefined;
      });
    }
    return res;
  }
}
