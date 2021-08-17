import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactAccountFaComponent } from '../list/contact-account-fa.component';
import { ContactAccountFaDetailComponent } from '../detail/contact-account-fa-detail.component';
import { ContactAccountFaUpdateComponent } from '../update/contact-account-fa-update.component';
import { ContactAccountFaRoutingResolveService } from './contact-account-fa-routing-resolve.service';

const contactAccountRoute: Routes = [
  {
    path: '',
    component: ContactAccountFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactAccountFaDetailComponent,
    resolve: {
      contactAccount: ContactAccountFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactAccountFaUpdateComponent,
    resolve: {
      contactAccount: ContactAccountFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactAccountFaUpdateComponent,
    resolve: {
      contactAccount: ContactAccountFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactAccountRoute)],
  exports: [RouterModule],
})
export class ContactAccountFaRoutingModule {}
