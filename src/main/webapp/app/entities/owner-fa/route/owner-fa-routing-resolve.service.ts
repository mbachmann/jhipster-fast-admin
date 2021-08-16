import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOwnerFa, OwnerFa } from '../owner-fa.model';
import { OwnerFaService } from '../service/owner-fa.service';

@Injectable({ providedIn: 'root' })
export class OwnerFaRoutingResolveService implements Resolve<IOwnerFa> {
  constructor(protected service: OwnerFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOwnerFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((owner: HttpResponse<OwnerFa>) => {
          if (owner.body) {
            return of(owner.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OwnerFa());
  }
}
