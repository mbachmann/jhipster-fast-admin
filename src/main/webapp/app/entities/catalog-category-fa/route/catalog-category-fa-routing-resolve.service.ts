import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogCategoryFa, CatalogCategoryFa } from '../catalog-category-fa.model';
import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';

@Injectable({ providedIn: 'root' })
export class CatalogCategoryFaRoutingResolveService implements Resolve<ICatalogCategoryFa> {
  constructor(protected service: CatalogCategoryFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogCategoryFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogCategory: HttpResponse<CatalogCategoryFa>) => {
          if (catalogCategory.body) {
            return of(catalogCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogCategoryFa());
  }
}
