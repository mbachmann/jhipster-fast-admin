import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissionMySuffix, PermissionMySuffix } from '../permission-my-suffix.model';
import { PermissionMySuffixService } from '../service/permission-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class PermissionMySuffixRoutingResolveService implements Resolve<IPermissionMySuffix> {
  constructor(protected service: PermissionMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermissionMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permission: HttpResponse<PermissionMySuffix>) => {
          if (permission.body) {
            return of(permission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PermissionMySuffix());
  }
}
