import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FreeTextComponent } from '../list/free-text.component';
import { FreeTextDetailComponent } from '../detail/free-text-detail.component';
import { FreeTextUpdateComponent } from '../update/free-text-update.component';
import { FreeTextRoutingResolveService } from './free-text-routing-resolve.service';

const freeTextRoute: Routes = [
  {
    path: '',
    component: FreeTextComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FreeTextDetailComponent,
    resolve: {
      freeText: FreeTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FreeTextUpdateComponent,
    resolve: {
      freeText: FreeTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FreeTextUpdateComponent,
    resolve: {
      freeText: FreeTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(freeTextRoute)],
  exports: [RouterModule],
})
export class FreeTextRoutingModule {}
