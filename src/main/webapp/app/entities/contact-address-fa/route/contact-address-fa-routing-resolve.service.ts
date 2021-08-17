import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactAddressFa, ContactAddressFa } from '../contact-address-fa.model';
import { ContactAddressFaService } from '../service/contact-address-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactAddressFaRoutingResolveService implements Resolve<IContactAddressFa> {
  constructor(protected service: ContactAddressFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactAddressFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactAddress: HttpResponse<ContactAddressFa>) => {
          if (contactAddress.body) {
            return of(contactAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactAddressFa());
  }
}
