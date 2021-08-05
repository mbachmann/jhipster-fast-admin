import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactReminderMySuffix, getContactReminderMySuffixIdentifier } from '../contact-reminder-my-suffix.model';

export type EntityResponseType = HttpResponse<IContactReminderMySuffix>;
export type EntityArrayResponseType = HttpResponse<IContactReminderMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ContactReminderMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-reminders');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-reminders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactReminder: IContactReminderMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .post<IContactReminderMySuffix>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactReminder: IContactReminderMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .put<IContactReminderMySuffix>(`${this.resourceUrl}/${getContactReminderMySuffixIdentifier(contactReminder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contactReminder: IContactReminderMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactReminder);
    return this.http
      .patch<IContactReminderMySuffix>(`${this.resourceUrl}/${getContactReminderMySuffixIdentifier(contactReminder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactReminderMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactReminderMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactReminderMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addContactReminderMySuffixToCollectionIfMissing(
    contactReminderCollection: IContactReminderMySuffix[],
    ...contactRemindersToCheck: (IContactReminderMySuffix | null | undefined)[]
  ): IContactReminderMySuffix[] {
    const contactReminders: IContactReminderMySuffix[] = contactRemindersToCheck.filter(isPresent);
    if (contactReminders.length > 0) {
      const contactReminderCollectionIdentifiers = contactReminderCollection.map(
        contactReminderItem => getContactReminderMySuffixIdentifier(contactReminderItem)!
      );
      const contactRemindersToAdd = contactReminders.filter(contactReminderItem => {
        const contactReminderIdentifier = getContactReminderMySuffixIdentifier(contactReminderItem);
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

  protected convertDateFromClient(contactReminder: IContactReminderMySuffix): IContactReminderMySuffix {
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
      res.body.forEach((contactReminder: IContactReminderMySuffix) => {
        contactReminder.dateTime = contactReminder.dateTime ? dayjs(contactReminder.dateTime) : undefined;
      });
    }
    return res;
  }
}
