import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOwnerMySuffix, OwnerMySuffix } from '../owner-my-suffix.model';
import { OwnerMySuffixService } from '../service/owner-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class OwnerMySuffixRoutingResolveService implements Resolve<IOwnerMySuffix> {
  constructor(protected service: OwnerMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOwnerMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((owner: HttpResponse<OwnerMySuffix>) => {
          if (owner.body) {
            return of(owner.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OwnerMySuffix());
  }
}
