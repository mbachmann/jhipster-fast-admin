import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomFieldFaComponent } from '../list/custom-field-fa.component';
import { CustomFieldFaDetailComponent } from '../detail/custom-field-fa-detail.component';
import { CustomFieldFaUpdateComponent } from '../update/custom-field-fa-update.component';
import { CustomFieldFaRoutingResolveService } from './custom-field-fa-routing-resolve.service';

const customFieldRoute: Routes = [
  {
    path: '',
    component: CustomFieldFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomFieldFaDetailComponent,
    resolve: {
      customField: CustomFieldFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomFieldFaUpdateComponent,
    resolve: {
      customField: CustomFieldFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomFieldFaUpdateComponent,
    resolve: {
      customField: CustomFieldFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customFieldRoute)],
  exports: [RouterModule],
})
export class CustomFieldFaRoutingModule {}
