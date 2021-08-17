import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomFieldValueComponent } from '../list/custom-field-value.component';
import { CustomFieldValueDetailComponent } from '../detail/custom-field-value-detail.component';
import { CustomFieldValueUpdateComponent } from '../update/custom-field-value-update.component';
import { CustomFieldValueRoutingResolveService } from './custom-field-value-routing-resolve.service';

const customFieldValueRoute: Routes = [
  {
    path: '',
    component: CustomFieldValueComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomFieldValueDetailComponent,
    resolve: {
      customFieldValue: CustomFieldValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomFieldValueUpdateComponent,
    resolve: {
      customFieldValue: CustomFieldValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomFieldValueUpdateComponent,
    resolve: {
      customFieldValue: CustomFieldValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customFieldValueRoute)],
  exports: [RouterModule],
})
export class CustomFieldValueRoutingModule {}
