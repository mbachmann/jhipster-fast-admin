import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactRelationMySuffix, ContactRelationMySuffix } from '../contact-relation-my-suffix.model';
import { ContactRelationMySuffixService } from '../service/contact-relation-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class ContactRelationMySuffixRoutingResolveService implements Resolve<IContactRelationMySuffix> {
  constructor(protected service: ContactRelationMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactRelationMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactRelation: HttpResponse<ContactRelationMySuffix>) => {
          if (contactRelation.body) {
            return of(contactRelation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactRelationMySuffix());
  }
}
