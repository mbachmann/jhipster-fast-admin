import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactRelationFaComponent } from '../list/contact-relation-fa.component';
import { ContactRelationFaDetailComponent } from '../detail/contact-relation-fa-detail.component';
import { ContactRelationFaUpdateComponent } from '../update/contact-relation-fa-update.component';
import { ContactRelationFaRoutingResolveService } from './contact-relation-fa-routing-resolve.service';

const contactRelationRoute: Routes = [
  {
    path: '',
    component: ContactRelationFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactRelationFaDetailComponent,
    resolve: {
      contactRelation: ContactRelationFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactRelationFaUpdateComponent,
    resolve: {
      contactRelation: ContactRelationFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactRelationFaUpdateComponent,
    resolve: {
      contactRelation: ContactRelationFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactRelationRoute)],
  exports: [RouterModule],
})
export class ContactRelationFaRoutingModule {}
