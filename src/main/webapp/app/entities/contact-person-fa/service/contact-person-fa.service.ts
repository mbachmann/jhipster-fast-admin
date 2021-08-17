import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactPersonFa, getContactPersonFaIdentifier } from '../contact-person-fa.model';

export type EntityResponseType = HttpResponse<IContactPersonFa>;
export type EntityArrayResponseType = HttpResponse<IContactPersonFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactPersonFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactPerson: IContactPersonFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactPerson);
    return this.http
      .post<IContactPersonFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactPerson: IContactPersonFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactPerson);
    return this.http
      .put<IContactPersonFa>(`${this.resourceUrl}/${getContactPersonFaIdentifier(contactPerson) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contactPerson: IContactPersonFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactPerson);
    return this.http
      .patch<IContactPersonFa>(`${this.resourceUrl}/${getContactPersonFaIdentifier(contactPerson) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactPersonFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactPersonFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
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

  protected convertDateFromClient(contactPerson: IContactPersonFa): IContactPersonFa {
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
      res.body.forEach((contactPerson: IContactPersonFa) => {
        contactPerson.birthDate = contactPerson.birthDate ? dayjs(contactPerson.birthDate) : undefined;
      });
    }
    return res;
  }
}
