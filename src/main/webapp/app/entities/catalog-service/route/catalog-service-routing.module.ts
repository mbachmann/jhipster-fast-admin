import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogServiceComponent } from '../list/catalog-service.component';
import { CatalogServiceDetailComponent } from '../detail/catalog-service-detail.component';
import { CatalogServiceUpdateComponent } from '../update/catalog-service-update.component';
import { CatalogServiceRoutingResolveService } from './catalog-service-routing-resolve.service';

const catalogServiceRoute: Routes = [
  {
    path: '',
    component: CatalogServiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogServiceDetailComponent,
    resolve: {
      catalogService: CatalogServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogServiceUpdateComponent,
    resolve: {
      catalogService: CatalogServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogServiceUpdateComponent,
    resolve: {
      catalogService: CatalogServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogServiceRoute)],
  exports: [RouterModule],
})
export class CatalogServiceRoutingModule {}
