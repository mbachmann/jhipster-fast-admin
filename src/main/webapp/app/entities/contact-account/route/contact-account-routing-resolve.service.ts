import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactAccount, ContactAccount } from '../contact-account.model';
import { ContactAccountService } from '../service/contact-account.service';

@Injectable({ providedIn: 'root' })
export class ContactAccountRoutingResolveService implements Resolve<IContactAccount> {
  constructor(protected service: ContactAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactAccount: HttpResponse<ContactAccount>) => {
          if (contactAccount.body) {
            return of(contactAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactAccount());
  }
}
