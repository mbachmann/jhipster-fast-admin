import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogCategoryComponent } from '../list/catalog-category.component';
import { CatalogCategoryDetailComponent } from '../detail/catalog-category-detail.component';
import { CatalogCategoryUpdateComponent } from '../update/catalog-category-update.component';
import { CatalogCategoryRoutingResolveService } from './catalog-category-routing-resolve.service';

const catalogCategoryRoute: Routes = [
  {
    path: '',
    component: CatalogCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogCategoryDetailComponent,
    resolve: {
      catalogCategory: CatalogCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogCategoryUpdateComponent,
    resolve: {
      catalogCategory: CatalogCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogCategoryUpdateComponent,
    resolve: {
      catalogCategory: CatalogCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogCategoryRoute)],
  exports: [RouterModule],
})
export class CatalogCategoryRoutingModule {}
