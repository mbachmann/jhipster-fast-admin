import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogServiceFaComponent } from '../list/catalog-service-fa.component';
import { CatalogServiceFaDetailComponent } from '../detail/catalog-service-fa-detail.component';
import { CatalogServiceFaUpdateComponent } from '../update/catalog-service-fa-update.component';
import { CatalogServiceFaRoutingResolveService } from './catalog-service-fa-routing-resolve.service';

const catalogServiceRoute: Routes = [
  {
    path: '',
    component: CatalogServiceFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogServiceFaDetailComponent,
    resolve: {
      catalogService: CatalogServiceFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogServiceFaUpdateComponent,
    resolve: {
      catalogService: CatalogServiceFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogServiceFaUpdateComponent,
    resolve: {
      catalogService: CatalogServiceFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogServiceRoute)],
  exports: [RouterModule],
})
export class CatalogServiceFaRoutingModule {}
