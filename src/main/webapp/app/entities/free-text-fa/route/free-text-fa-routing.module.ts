import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FreeTextFaComponent } from '../list/free-text-fa.component';
import { FreeTextFaDetailComponent } from '../detail/free-text-fa-detail.component';
import { FreeTextFaUpdateComponent } from '../update/free-text-fa-update.component';
import { FreeTextFaRoutingResolveService } from './free-text-fa-routing-resolve.service';

const freeTextRoute: Routes = [
  {
    path: '',
    component: FreeTextFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FreeTextFaDetailComponent,
    resolve: {
      freeText: FreeTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FreeTextFaUpdateComponent,
    resolve: {
      freeText: FreeTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FreeTextFaUpdateComponent,
    resolve: {
      freeText: FreeTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(freeTextRoute)],
  exports: [RouterModule],
})
export class FreeTextFaRoutingModule {}
