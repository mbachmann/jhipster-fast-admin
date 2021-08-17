import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactAccountFa, ContactAccountFa } from '../contact-account-fa.model';
import { ContactAccountFaService } from '../service/contact-account-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactAccountFaRoutingResolveService implements Resolve<IContactAccountFa> {
  constructor(protected service: ContactAccountFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactAccountFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactAccount: HttpResponse<ContactAccountFa>) => {
          if (contactAccount.body) {
            return of(contactAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactAccountFa());
  }
}
