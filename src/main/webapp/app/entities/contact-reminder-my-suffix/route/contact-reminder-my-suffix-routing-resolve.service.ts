import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactReminderMySuffix, ContactReminderMySuffix } from '../contact-reminder-my-suffix.model';
import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class ContactReminderMySuffixRoutingResolveService implements Resolve<IContactReminderMySuffix> {
  constructor(protected service: ContactReminderMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactReminderMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactReminder: HttpResponse<ContactReminderMySuffix>) => {
          if (contactReminder.body) {
            return of(contactReminder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactReminderMySuffix());
  }
}
