import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactFaComponent } from '../list/contact-fa.component';
import { ContactFaDetailComponent } from '../detail/contact-fa-detail.component';
import { ContactFaUpdateComponent } from '../update/contact-fa-update.component';
import { ContactFaRoutingResolveService } from './contact-fa-routing-resolve.service';

const contactRoute: Routes = [
  {
    path: '',
    component: ContactFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactFaDetailComponent,
    resolve: {
      contact: ContactFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactFaUpdateComponent,
    resolve: {
      contact: ContactFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactFaUpdateComponent,
    resolve: {
      contact: ContactFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactRoute)],
  exports: [RouterModule],
})
export class ContactFaRoutingModule {}
