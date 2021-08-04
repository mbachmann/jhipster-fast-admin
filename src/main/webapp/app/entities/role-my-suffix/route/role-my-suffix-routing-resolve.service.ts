import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoleMySuffix, RoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class RoleMySuffixRoutingResolveService implements Resolve<IRoleMySuffix> {
  constructor(protected service: RoleMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoleMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((role: HttpResponse<RoleMySuffix>) => {
          if (role.body) {
            return of(role.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RoleMySuffix());
  }
}
