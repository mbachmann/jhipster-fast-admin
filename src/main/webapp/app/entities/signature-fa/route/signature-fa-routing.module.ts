import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SignatureFaComponent } from '../list/signature-fa.component';
import { SignatureFaDetailComponent } from '../detail/signature-fa-detail.component';
import { SignatureFaUpdateComponent } from '../update/signature-fa-update.component';
import { SignatureFaRoutingResolveService } from './signature-fa-routing-resolve.service';

const signatureRoute: Routes = [
  {
    path: '',
    component: SignatureFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SignatureFaDetailComponent,
    resolve: {
      signature: SignatureFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SignatureFaUpdateComponent,
    resolve: {
      signature: SignatureFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SignatureFaUpdateComponent,
    resolve: {
      signature: SignatureFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(signatureRoute)],
  exports: [RouterModule],
})
export class SignatureFaRoutingModule {}
