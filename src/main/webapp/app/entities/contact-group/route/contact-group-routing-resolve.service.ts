import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactGroup, ContactGroup } from '../contact-group.model';
import { ContactGroupService } from '../service/contact-group.service';

@Injectable({ providedIn: 'root' })
export class ContactGroupRoutingResolveService implements Resolve<IContactGroup> {
  constructor(protected service: ContactGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactGroup: HttpResponse<ContactGroup>) => {
          if (contactGroup.body) {
            return of(contactGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactGroup());
  }
}
