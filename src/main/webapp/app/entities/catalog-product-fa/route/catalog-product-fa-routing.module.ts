import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogProductFaComponent } from '../list/catalog-product-fa.component';
import { CatalogProductFaDetailComponent } from '../detail/catalog-product-fa-detail.component';
import { CatalogProductFaUpdateComponent } from '../update/catalog-product-fa-update.component';
import { CatalogProductFaRoutingResolveService } from './catalog-product-fa-routing-resolve.service';

const catalogProductRoute: Routes = [
  {
    path: '',
    component: CatalogProductFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogProductFaDetailComponent,
    resolve: {
      catalogProduct: CatalogProductFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogProductFaUpdateComponent,
    resolve: {
      catalogProduct: CatalogProductFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogProductFaUpdateComponent,
    resolve: {
      catalogProduct: CatalogProductFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogProductRoute)],
  exports: [RouterModule],
})
export class CatalogProductFaRoutingModule {}
