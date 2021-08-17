import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourcePermission, ResourcePermission } from '../resource-permission.model';
import { ResourcePermissionService } from '../service/resource-permission.service';

@Injectable({ providedIn: 'root' })
export class ResourcePermissionRoutingResolveService implements Resolve<IResourcePermission> {
  constructor(protected service: ResourcePermissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResourcePermission> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resourcePermission: HttpResponse<ResourcePermission>) => {
          if (resourcePermission.body) {
            return of(resourcePermission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResourcePermission());
  }
}
