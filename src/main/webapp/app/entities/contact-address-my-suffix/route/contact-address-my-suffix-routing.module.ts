import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactAddressMySuffixComponent } from '../list/contact-address-my-suffix.component';
import { ContactAddressMySuffixDetailComponent } from '../detail/contact-address-my-suffix-detail.component';
import { ContactAddressMySuffixUpdateComponent } from '../update/contact-address-my-suffix-update.component';
import { ContactAddressMySuffixRoutingResolveService } from './contact-address-my-suffix-routing-resolve.service';

const contactAddressRoute: Routes = [
  {
    path: '',
    component: ContactAddressMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactAddressMySuffixDetailComponent,
    resolve: {
      contactAddress: ContactAddressMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactAddressMySuffixUpdateComponent,
    resolve: {
      contactAddress: ContactAddressMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactAddressMySuffixUpdateComponent,
    resolve: {
      contactAddress: ContactAddressMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactAddressRoute)],
  exports: [RouterModule],
})
export class ContactAddressMySuffixRoutingModule {}
