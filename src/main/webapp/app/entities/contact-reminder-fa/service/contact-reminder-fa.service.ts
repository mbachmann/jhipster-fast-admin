import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactReminderFa, getContactReminderFaIdentifier } from '../contact-reminder-fa.model';

export type EntityResponseType = HttpResponse<IContactReminderFa>;
export type EntityArrayResponseType = HttpResponse<IContactReminderFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactReminderFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-reminders');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-reminders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactReminder: IContactReminderFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .post<IContactReminderFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactReminder: IContactReminderFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .put<IContactReminderFa>(`${this.resourceUrl}/${getContactReminderFaIdentifier(contactReminder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contactReminder: IContactReminderFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .patch<IContactReminderFa>(`${this.resourceUrl}/${getContactReminderFaIdentifier(contactReminder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactReminderFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactReminderFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactReminderFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addContactReminderFaToCollectionIfMissing(
    contactReminderCollection: IContactReminderFa[],
    ...contactRemindersToCheck: (IContactReminderFa | null | undefined)[]
  ): IContactReminderFa[] {
    const contactReminders: IContactReminderFa[] = contactRemindersToCheck.filter(isPresent);
    if (contactReminders.length > 0) {
      const contactReminderCollectionIdentifiers = contactReminderCollection.map(
        contactReminderItem => getContactReminderFaIdentifier(contactReminderItem)!
      );
      const contactRemindersToAdd = contactReminders.filter(contactReminderItem => {
        const contactReminderIdentifier = getContactReminderFaIdentifier(contactReminderItem);
        if (contactReminderIdentifier == null || contactReminderCollectionIdentifiers.includes(contactReminderIdentifier)) {
          return false;
        }
        contactReminderCollectionIdentifiers.push(contactReminderIdentifier);
        return true;
      });
      return [...contactRemindersToAdd, ...contactReminderCollection];
    }
    return contactReminderCollection;
  }

  protected convertDateFromClient(contactReminder: IContactReminderFa): IContactReminderFa {
    return Object.assign({}, contactReminder, {
      dateTime: contactReminder.dateTime?.isValid() ? contactReminder.dateTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateTime = res.body.dateTime ? dayjs(res.body.dateTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contactReminder: IContactReminderFa) => {
        contactReminder.dateTime = contactReminder.dateTime ? dayjs(contactReminder.dateTime) : undefined;
      });
    }
    return res;
  }
}
