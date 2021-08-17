import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactAddress, ContactAddress } from '../contact-address.model';
import { ContactAddressService } from '../service/contact-address.service';

@Injectable({ providedIn: 'root' })
export class ContactAddressRoutingResolveService implements Resolve<IContactAddress> {
  constructor(protected service: ContactAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactAddress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactAddress: HttpResponse<ContactAddress>) => {
          if (contactAddress.body) {
            return of(contactAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactAddress());
  }
}
