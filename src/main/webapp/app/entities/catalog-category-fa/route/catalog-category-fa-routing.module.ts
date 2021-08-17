import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogCategoryFaComponent } from '../list/catalog-category-fa.component';
import { CatalogCategoryFaDetailComponent } from '../detail/catalog-category-fa-detail.component';
import { CatalogCategoryFaUpdateComponent } from '../update/catalog-category-fa-update.component';
import { CatalogCategoryFaRoutingResolveService } from './catalog-category-fa-routing-resolve.service';

const catalogCategoryRoute: Routes = [
  {
    path: '',
    component: CatalogCategoryFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogCategoryFaDetailComponent,
    resolve: {
      catalogCategory: CatalogCategoryFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogCategoryFaUpdateComponent,
    resolve: {
      catalogCategory: CatalogCategoryFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogCategoryFaUpdateComponent,
    resolve: {
      catalogCategory: CatalogCategoryFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogCategoryRoute)],
  exports: [RouterModule],
})
export class CatalogCategoryFaRoutingModule {}
