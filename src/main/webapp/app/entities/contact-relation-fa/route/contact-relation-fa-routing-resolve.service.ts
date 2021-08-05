import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactRelationFa, ContactRelationFa } from '../contact-relation-fa.model';
import { ContactRelationFaService } from '../service/contact-relation-fa.service';

@Injectable({ providedIn: 'root' })
export class ContactRelationFaRoutingResolveService implements Resolve<IContactRelationFa> {
  constructor(protected service: ContactRelationFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactRelationFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactRelation: HttpResponse<ContactRelationFa>) => {
          if (contactRelation.body) {
            return of(contactRelation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactRelationFa());
  }
}
