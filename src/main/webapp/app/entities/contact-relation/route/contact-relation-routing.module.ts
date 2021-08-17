import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactRelationComponent } from '../list/contact-relation.component';
import { ContactRelationDetailComponent } from '../detail/contact-relation-detail.component';
import { ContactRelationUpdateComponent } from '../update/contact-relation-update.component';
import { ContactRelationRoutingResolveService } from './contact-relation-routing-resolve.service';

const contactRelationRoute: Routes = [
  {
    path: '',
    component: ContactRelationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactRelationDetailComponent,
    resolve: {
      contactRelation: ContactRelationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactRelationUpdateComponent,
    resolve: {
      contactRelation: ContactRelationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactRelationUpdateComponent,
    resolve: {
      contactRelation: ContactRelationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactRelationRoute)],
  exports: [RouterModule],
})
export class ContactRelationRoutingModule {}
