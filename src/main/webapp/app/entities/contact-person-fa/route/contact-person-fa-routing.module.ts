import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactPersonFaComponent } from '../list/contact-person-fa.component';
import { ContactPersonFaDetailComponent } from '../detail/contact-person-fa-detail.component';
import { ContactPersonFaUpdateComponent } from '../update/contact-person-fa-update.component';
import { ContactPersonFaRoutingResolveService } from './contact-person-fa-routing-resolve.service';

const contactPersonRoute: Routes = [
  {
    path: '',
    component: ContactPersonFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactPersonFaDetailComponent,
    resolve: {
      contactPerson: ContactPersonFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactPersonFaUpdateComponent,
    resolve: {
      contactPerson: ContactPersonFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactPersonFaUpdateComponent,
    resolve: {
      contactPerson: ContactPersonFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactPersonRoute)],
  exports: [RouterModule],
})
export class ContactPersonFaRoutingModule {}
