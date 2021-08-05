import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactMySuffixComponent } from '../list/contact-my-suffix.component';
import { ContactMySuffixDetailComponent } from '../detail/contact-my-suffix-detail.component';
import { ContactMySuffixUpdateComponent } from '../update/contact-my-suffix-update.component';
import { ContactMySuffixRoutingResolveService } from './contact-my-suffix-routing-resolve.service';

const contactRoute: Routes = [
  {
    path: '',
    component: ContactMySuffixComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactMySuffixDetailComponent,
    resolve: {
      contact: ContactMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactMySuffixUpdateComponent,
    resolve: {
      contact: ContactMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactMySuffixUpdateComponent,
    resolve: {
      contact: ContactMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactRoute)],
  exports: [RouterModule],
})
export class ContactMySuffixRoutingModule {}
