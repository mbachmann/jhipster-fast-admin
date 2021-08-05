import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactAddressMySuffix, ContactAddressMySuffix } from '../contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from '../service/contact-address-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class ContactAddressMySuffixRoutingResolveService implements Resolve<IContactAddressMySuffix> {
  constructor(protected service: ContactAddressMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactAddressMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactAddress: HttpResponse<ContactAddressMySuffix>) => {
          if (contactAddress.body) {
            return of(contactAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactAddressMySuffix());
  }
}
