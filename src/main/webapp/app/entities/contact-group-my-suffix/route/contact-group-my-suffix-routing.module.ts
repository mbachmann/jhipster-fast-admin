import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactGroupMySuffixComponent } from '../list/contact-group-my-suffix.component';
import { ContactGroupMySuffixDetailComponent } from '../detail/contact-group-my-suffix-detail.component';
import { ContactGroupMySuffixUpdateComponent } from '../update/contact-group-my-suffix-update.component';
import { ContactGroupMySuffixRoutingResolveService } from './contact-group-my-suffix-routing-resolve.service';

const contactGroupRoute: Routes = [
  {
    path: '',
    component: ContactGroupMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactGroupMySuffixDetailComponent,
    resolve: {
      contactGroup: ContactGroupMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactGroupMySuffixUpdateComponent,
    resolve: {
      contactGroup: ContactGroupMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactGroupMySuffixUpdateComponent,
    resolve: {
      contactGroup: ContactGroupMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactGroupRoute)],
  exports: [RouterModule],
})
export class ContactGroupMySuffixRoutingModule {}
