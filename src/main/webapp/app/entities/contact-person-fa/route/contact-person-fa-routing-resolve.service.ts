import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactPersonFa, ContactPersonFa } from '../contact-person-fa.model';
import { ContactPersonFaService } from '../service/contact-person-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactPersonFaRoutingResolveService implements Resolve<IContactPersonFa> {
  constructor(protected service: ContactPersonFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactPersonFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactPerson: HttpResponse<ContactPersonFa>) => {
          if (contactPerson.body) {
            return of(contactPerson.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactPersonFa());
  }
}
