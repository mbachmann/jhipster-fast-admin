import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactPersonMySuffix, ContactPersonMySuffix } from '../contact-person-my-suffix.model';
import { ContactPersonMySuffixService } from '../service/contact-person-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class ContactPersonMySuffixRoutingResolveService implements Resolve<IContactPersonMySuffix> {
  constructor(protected service: ContactPersonMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactPersonMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactPerson: HttpResponse<ContactPersonMySuffix>) => {
          if (contactPerson.body) {
            return of(contactPerson.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactPersonMySuffix());
  }
}
