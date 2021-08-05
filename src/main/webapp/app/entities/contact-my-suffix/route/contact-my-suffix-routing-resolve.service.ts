import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactMySuffix, ContactMySuffix } from '../contact-my-suffix.model';
import { ContactMySuffixService } from '../service/contact-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class ContactMySuffixRoutingResolveService implements Resolve<IContactMySuffix> {
  constructor(protected service: ContactMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contact: HttpResponse<ContactMySuffix>) => {
          if (contact.body) {
            return of(contact.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactMySuffix());
  }
}
