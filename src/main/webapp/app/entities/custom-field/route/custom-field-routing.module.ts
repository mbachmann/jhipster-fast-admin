import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomFieldComponent } from '../list/custom-field.component';
import { CustomFieldDetailComponent } from '../detail/custom-field-detail.component';
import { CustomFieldUpdateComponent } from '../update/custom-field-update.component';
import { CustomFieldRoutingResolveService } from './custom-field-routing-resolve.service';

const customFieldRoute: Routes = [
  {
    path: '',
    component: CustomFieldComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomFieldDetailComponent,
    resolve: {
      customField: CustomFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomFieldUpdateComponent,
    resolve: {
      customField: CustomFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomFieldUpdateComponent,
    resolve: {
      customField: CustomFieldRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customFieldRoute)],
  exports: [RouterModule],
})
export class CustomFieldRoutingModule {}
