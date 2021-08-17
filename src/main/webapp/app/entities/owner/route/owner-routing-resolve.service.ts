import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOwner, Owner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

@Injectable({ providedIn: 'root' })
export class OwnerRoutingResolveService implements Resolve<IOwner> {
  constructor(protected service: OwnerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOwner> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((owner: HttpResponse<Owner>) => {
          if (owner.body) {
            return of(owner.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Owner());
  }
}
