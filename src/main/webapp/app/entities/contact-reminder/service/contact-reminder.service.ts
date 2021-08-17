import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactReminder, getContactReminderIdentifier } from '../contact-reminder.model';

export type EntityResponseType = HttpResponse<IContactReminder>;
export type EntityArrayResponseType = HttpResponse<IContactReminder[]>;

@Injectable({ providedIn: 'root' })
export class ContactReminderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-reminders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactReminder: IContactReminder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .post<IContactReminder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactReminder: IContactReminder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .put<IContactReminder>(`${this.resourceUrl}/${getContactReminderIdentifier(contactReminder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contactReminder: IContactReminder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .patch<IContactReminder>(`${this.resourceUrl}/${getContactReminderIdentifier(contactReminder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactReminder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactReminder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactReminderToCollectionIfMissing(
    contactReminderCollection: IContactReminder[],
    ...contactRemindersToCheck: (IContactReminder | null | undefined)[]
  ): IContactReminder[] {
    const contactReminders: IContactReminder[] = contactRemindersToCheck.filter(isPresent);
    if (contactReminders.length > 0) {
      const contactReminderCollectionIdentifiers = contactReminderCollection.map(
        contactReminderItem => getContactReminderIdentifier(contactReminderItem)!
      );
      const contactRemindersToAdd = contactReminders.filter(contactReminderItem => {
        const contactReminderIdentifier = getContactReminderIdentifier(contactReminderItem);
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

  protected convertDateFromClient(contactReminder: IContactReminder): IContactReminder {
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
      res.body.forEach((contactReminder: IContactReminder) => {
        contactReminder.dateTime = contactReminder.dateTime ? dayjs(contactReminder.dateTime) : undefined;
      });
    }
    return res;
  }
}
