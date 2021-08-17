import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactAddressComponent } from '../list/contact-address.component';
import { ContactAddressDetailComponent } from '../detail/contact-address-detail.component';
import { ContactAddressUpdateComponent } from '../update/contact-address-update.component';
import { ContactAddressRoutingResolveService } from './contact-address-routing-resolve.service';

const contactAddressRoute: Routes = [
  {
    path: '',
    component: ContactAddressComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactAddressDetailComponent,
    resolve: {
      contactAddress: ContactAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactAddressUpdateComponent,
    resolve: {
      contactAddress: ContactAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactAddressUpdateComponent,
    resolve: {
      contactAddress: ContactAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactAddressRoute)],
  exports: [RouterModule],
})
export class ContactAddressRoutingModule {}
