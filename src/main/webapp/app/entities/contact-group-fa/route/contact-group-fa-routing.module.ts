import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactGroupFaComponent } from '../list/contact-group-fa.component';
import { ContactGroupFaDetailComponent } from '../detail/contact-group-fa-detail.component';
import { ContactGroupFaUpdateComponent } from '../update/contact-group-fa-update.component';
import { ContactGroupFaRoutingResolveService } from './contact-group-fa-routing-resolve.service';

const contactGroupRoute: Routes = [
  {
    path: '',
    component: ContactGroupFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactGroupFaDetailComponent,
    resolve: {
      contactGroup: ContactGroupFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactGroupFaUpdateComponent,
    resolve: {
      contactGroup: ContactGroupFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactGroupFaUpdateComponent,
    resolve: {
      contactGroup: ContactGroupFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactGroupRoute)],
  exports: [RouterModule],
})
export class ContactGroupFaRoutingModule {}
