import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogCategory, CatalogCategory } from '../catalog-category.model';
import { CatalogCategoryService } from '../service/catalog-category.service';

@Injectable({ providedIn: 'root' })
export class CatalogCategoryRoutingResolveService implements Resolve<ICatalogCategory> {
  constructor(protected service: CatalogCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogCategory: HttpResponse<CatalogCategory>) => {
          if (catalogCategory.body) {
            return of(catalogCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogCategory());
  }
}
