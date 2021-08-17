import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactAddressFaComponent } from '../list/contact-address-fa.component';
import { ContactAddressFaDetailComponent } from '../detail/contact-address-fa-detail.component';
import { ContactAddressFaUpdateComponent } from '../update/contact-address-fa-update.component';
import { ContactAddressFaRoutingResolveService } from './contact-address-fa-routing-resolve.service';

const contactAddressRoute: Routes = [
  {
    path: '',
    component: ContactAddressFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactAddressFaDetailComponent,
    resolve: {
      contactAddress: ContactAddressFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactAddressFaUpdateComponent,
    resolve: {
      contactAddress: ContactAddressFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactAddressFaUpdateComponent,
    resolve: {
      contactAddress: ContactAddressFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactAddressRoute)],
  exports: [RouterModule],
})
export class ContactAddressFaRoutingModule {}
