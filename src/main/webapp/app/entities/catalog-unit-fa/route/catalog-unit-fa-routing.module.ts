import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogUnitFaComponent } from '../list/catalog-unit-fa.component';
import { CatalogUnitFaDetailComponent } from '../detail/catalog-unit-fa-detail.component';
import { CatalogUnitFaUpdateComponent } from '../update/catalog-unit-fa-update.component';
import { CatalogUnitFaRoutingResolveService } from './catalog-unit-fa-routing-resolve.service';

const catalogUnitRoute: Routes = [
  {
    path: '',
    component: CatalogUnitFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogUnitFaDetailComponent,
    resolve: {
      catalogUnit: CatalogUnitFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogUnitFaUpdateComponent,
    resolve: {
      catalogUnit: CatalogUnitFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogUnitFaUpdateComponent,
    resolve: {
      catalogUnit: CatalogUnitFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogUnitRoute)],
  exports: [RouterModule],
})
export class CatalogUnitFaRoutingModule {}
