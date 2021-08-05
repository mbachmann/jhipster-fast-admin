import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactPersonMySuffixComponent } from '../list/contact-person-my-suffix.component';
import { ContactPersonMySuffixDetailComponent } from '../detail/contact-person-my-suffix-detail.component';
import { ContactPersonMySuffixUpdateComponent } from '../update/contact-person-my-suffix-update.component';
import { ContactPersonMySuffixRoutingResolveService } from './contact-person-my-suffix-routing-resolve.service';

const contactPersonRoute: Routes = [
  {
    path: '',
    component: ContactPersonMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactPersonMySuffixDetailComponent,
    resolve: {
      contactPerson: ContactPersonMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactPersonMySuffixUpdateComponent,
    resolve: {
      contactPerson: ContactPersonMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactPersonMySuffixUpdateComponent,
    resolve: {
      contactPerson: ContactPersonMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactPersonRoute)],
  exports: [RouterModule],
})
export class ContactPersonMySuffixRoutingModule {}
