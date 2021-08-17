import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogProductComponent } from '../list/catalog-product.component';
import { CatalogProductDetailComponent } from '../detail/catalog-product-detail.component';
import { CatalogProductUpdateComponent } from '../update/catalog-product-update.component';
import { CatalogProductRoutingResolveService } from './catalog-product-routing-resolve.service';

const catalogProductRoute: Routes = [
  {
    path: '',
    component: CatalogProductComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogProductDetailComponent,
    resolve: {
      catalogProduct: CatalogProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogProductUpdateComponent,
    resolve: {
      catalogProduct: CatalogProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogProductUpdateComponent,
    resolve: {
      catalogProduct: CatalogProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogProductRoute)],
  exports: [RouterModule],
})
export class CatalogProductRoutingModule {}
