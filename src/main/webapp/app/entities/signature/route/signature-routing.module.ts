import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SignatureComponent } from '../list/signature.component';
import { SignatureDetailComponent } from '../detail/signature-detail.component';
import { SignatureUpdateComponent } from '../update/signature-update.component';
import { SignatureRoutingResolveService } from './signature-routing-resolve.service';

const signatureRoute: Routes = [
  {
    path: '',
    component: SignatureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SignatureDetailComponent,
    resolve: {
      signature: SignatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SignatureUpdateComponent,
    resolve: {
      signature: SignatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SignatureUpdateComponent,
    resolve: {
      signature: SignatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(signatureRoute)],
  exports: [RouterModule],
})
export class SignatureRoutingModule {}
