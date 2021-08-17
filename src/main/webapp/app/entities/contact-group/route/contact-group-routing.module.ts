import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactGroupComponent } from '../list/contact-group.component';
import { ContactGroupDetailComponent } from '../detail/contact-group-detail.component';
import { ContactGroupUpdateComponent } from '../update/contact-group-update.component';
import { ContactGroupRoutingResolveService } from './contact-group-routing-resolve.service';

const contactGroupRoute: Routes = [
  {
    path: '',
    component: ContactGroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactGroupDetailComponent,
    resolve: {
      contactGroup: ContactGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactGroupUpdateComponent,
    resolve: {
      contactGroup: ContactGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactGroupUpdateComponent,
    resolve: {
      contactGroup: ContactGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactGroupRoute)],
  exports: [RouterModule],
})
export class ContactGroupRoutingModule {}
