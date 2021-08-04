import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactGroupMySuffix, ContactGroupMySuffix } from '../contact-group-my-suffix.model';
import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class ContactGroupMySuffixRoutingResolveService implements Resolve<IContactGroupMySuffix> {
  constructor(protected service: ContactGroupMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactGroupMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactGroup: HttpResponse<ContactGroupMySuffix>) => {
          if (contactGroup.body) {
            return of(contactGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactGroupMySuffix());
  }
}
