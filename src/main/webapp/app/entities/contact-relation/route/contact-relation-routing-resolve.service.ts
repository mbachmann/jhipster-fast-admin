import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactRelation, ContactRelation } from '../contact-relation.model';
import { ContactRelationService } from '../service/contact-relation.service';

@Injectable({ providedIn: 'root' })
export class ContactRelationRoutingResolveService implements Resolve<IContactRelation> {
  constructor(protected service: ContactRelationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactRelation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactRelation: HttpResponse<ContactRelation>) => {
          if (contactRelation.body) {
            return of(contactRelation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactRelation());
  }
}
