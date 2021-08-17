import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicationRole, ApplicationRole } from '../application-role.model';
import { ApplicationRoleService } from '../service/application-role.service';

@Injectable({ providedIn: 'root' })
export class ApplicationRoleRoutingResolveService implements Resolve<IApplicationRole> {
  constructor(protected service: ApplicationRoleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicationRole> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((applicationRole: HttpResponse<ApplicationRole>) => {
          if (applicationRole.body) {
            return of(applicationRole.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApplicationRole());
  }
}
