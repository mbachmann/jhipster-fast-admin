import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogService, CatalogService } from '../catalog-service.model';
import { CatalogServiceService } from '../service/catalog-service.service';

@Injectable({ providedIn: 'root' })
export class CatalogServiceRoutingResolveService implements Resolve<ICatalogService> {
  constructor(protected service: CatalogServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogService: HttpResponse<CatalogService>) => {
          if (catalogService.body) {
            return of(catalogService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogService());
  }
}
