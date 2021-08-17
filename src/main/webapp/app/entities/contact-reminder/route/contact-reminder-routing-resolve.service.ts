import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactReminder, ContactReminder } from '../contact-reminder.model';
import { ContactReminderService } from '../service/contact-reminder.service';

@Injectable({ providedIn: 'root' })
export class ContactReminderRoutingResolveService implements Resolve<IContactReminder> {
  constructor(protected service: ContactReminderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactReminder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactReminder: HttpResponse<ContactReminder>) => {
          if (contactReminder.body) {
            return of(contactReminder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactReminder());
  }
}
