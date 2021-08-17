import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogUnitComponent } from '../list/catalog-unit.component';
import { CatalogUnitDetailComponent } from '../detail/catalog-unit-detail.component';
import { CatalogUnitUpdateComponent } from '../update/catalog-unit-update.component';
import { CatalogUnitRoutingResolveService } from './catalog-unit-routing-resolve.service';

const catalogUnitRoute: Routes = [
  {
    path: '',
    component: CatalogUnitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogUnitDetailComponent,
    resolve: {
      catalogUnit: CatalogUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogUnitUpdateComponent,
    resolve: {
      catalogUnit: CatalogUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogUnitUpdateComponent,
    resolve: {
      catalogUnit: CatalogUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogUnitRoute)],
  exports: [RouterModule],
})
export class CatalogUnitRoutingModule {}
