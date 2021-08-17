import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactFa, ContactFa } from '../contact-fa.model';
import { ContactFaService } from '../service/contact-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactFaRoutingResolveService implements Resolve<IContactFa> {
  constructor(protected service: ContactFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contact: HttpResponse<ContactFa>) => {
          if (contact.body) {
            return of(contact.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactFa());
  }
}
