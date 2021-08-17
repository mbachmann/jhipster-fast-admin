import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactReminderFa, ContactReminderFa } from '../contact-reminder-fa.model';
import { ContactReminderFaService } from '../service/contact-reminder-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactReminderFaRoutingResolveService implements Resolve<IContactReminderFa> {
  constructor(protected service: ContactReminderFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactReminderFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactReminder: HttpResponse<ContactReminderFa>) => {
          if (contactReminder.body) {
            return of(contactReminder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactReminderFa());
  }
}
