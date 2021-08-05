import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoleFa, RoleFa } from '../role-fa.model';
import { RoleFaService } from '../service/role-fa.service';

@Injectable({ providedIn: 'root' })
export class RoleFaRoutingResolveService implements Resolve<IRoleFa> {
  constructor(protected service: RoleFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoleFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((role: HttpResponse<RoleFa>) => {
          if (role.body) {
            return of(role.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RoleFa());
  }
}
