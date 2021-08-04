import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomFieldMySuffixComponent } from '../list/custom-field-my-suffix.component';
import { CustomFieldMySuffixDetailComponent } from '../detail/custom-field-my-suffix-detail.component';
import { CustomFieldMySuffixUpdateComponent } from '../update/custom-field-my-suffix-update.component';
import { CustomFieldMySuffixRoutingResolveService } from './custom-field-my-suffix-routing-resolve.service';

const customFieldRoute: Routes = [
  {
    path: '',
    component: CustomFieldMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomFieldMySuffixDetailComponent,
    resolve: {
      customField: CustomFieldMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomFieldMySuffixUpdateComponent,
    resolve: {
      customField: CustomFieldMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomFieldMySuffixUpdateComponent,
    resolve: {
      customField: CustomFieldMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customFieldRoute)],
  exports: [RouterModule],
})
export class CustomFieldMySuffixRoutingModule {}
