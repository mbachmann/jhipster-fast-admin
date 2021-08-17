import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogServiceFa, CatalogServiceFa } from '../catalog-service-fa.model';
import { CatalogServiceFaService } from '../service/catalog-service-fa.service';

@Injectable({ providedIn: 'root' })
export class CatalogServiceFaRoutingResolveService implements Resolve<ICatalogServiceFa> {
  constructor(protected service: CatalogServiceFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogServiceFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogService: HttpResponse<CatalogServiceFa>) => {
          if (catalogService.body) {
            return of(catalogService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogServiceFa());
  }
}
