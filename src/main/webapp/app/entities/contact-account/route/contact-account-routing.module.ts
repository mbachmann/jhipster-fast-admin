import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactAccountComponent } from '../list/contact-account.component';
import { ContactAccountDetailComponent } from '../detail/contact-account-detail.component';
import { ContactAccountUpdateComponent } from '../update/contact-account-update.component';
import { ContactAccountRoutingResolveService } from './contact-account-routing-resolve.service';

const contactAccountRoute: Routes = [
  {
    path: '',
    component: ContactAccountComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactAccountDetailComponent,
    resolve: {
      contactAccount: ContactAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactAccountUpdateComponent,
    resolve: {
      contactAccount: ContactAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactAccountUpdateComponent,
    resolve: {
      contactAccount: ContactAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactAccountRoute)],
  exports: [RouterModule],
})
export class ContactAccountRoutingModule {}
