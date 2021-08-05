import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactRelationMySuffixComponent } from '../list/contact-relation-my-suffix.component';
import { ContactRelationMySuffixDetailComponent } from '../detail/contact-relation-my-suffix-detail.component';
import { ContactRelationMySuffixUpdateComponent } from '../update/contact-relation-my-suffix-update.component';
import { ContactRelationMySuffixRoutingResolveService } from './contact-relation-my-suffix-routing-resolve.service';

const contactRelationRoute: Routes = [
  {
    path: '',
    component: ContactRelationMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactRelationMySuffixDetailComponent,
    resolve: {
      contactRelation: ContactRelationMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactRelationMySuffixUpdateComponent,
    resolve: {
      contactRelation: ContactRelationMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactRelationMySuffixUpdateComponent,
    resolve: {
      contactRelation: ContactRelationMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactRelationRoute)],
  exports: [RouterModule],
})
export class ContactRelationMySuffixRoutingModule {}
