import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactGroupFa, ContactGroupFa } from '../contact-group-fa.model';
import { ContactGroupFaService } from '../service/contact-group-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactGroupFaRoutingResolveService implements Resolve<IContactGroupFa> {
  constructor(protected service: ContactGroupFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactGroupFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactGroup: HttpResponse<ContactGroupFa>) => {
          if (contactGroup.body) {
            return of(contactGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactGroupFa());
  }
}
