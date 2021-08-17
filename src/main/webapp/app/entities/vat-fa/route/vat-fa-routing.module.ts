import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VatFaComponent } from '../list/vat-fa.component';
import { VatFaDetailComponent } from '../detail/vat-fa-detail.component';
import { VatFaUpdateComponent } from '../update/vat-fa-update.component';
import { VatFaRoutingResolveService } from './vat-fa-routing-resolve.service';

const vatRoute: Routes = [
  {
    path: '',
    component: VatFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VatFaDetailComponent,
    resolve: {
      vat: VatFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VatFaUpdateComponent,
    resolve: {
      vat: VatFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VatFaUpdateComponent,
    resolve: {
      vat: VatFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vatRoute)],
  exports: [RouterModule],
})
export class VatFaRoutingModule {}
