import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DescriptiveDocumentTextFaComponent } from '../list/descriptive-document-text-fa.component';
import { DescriptiveDocumentTextFaDetailComponent } from '../detail/descriptive-document-text-fa-detail.component';
import { DescriptiveDocumentTextFaUpdateComponent } from '../update/descriptive-document-text-fa-update.component';
import { DescriptiveDocumentTextFaRoutingResolveService } from './descriptive-document-text-fa-routing-resolve.service';

const descriptiveDocumentTextRoute: Routes = [
  {
    path: '',
    component: DescriptiveDocumentTextFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DescriptiveDocumentTextFaDetailComponent,
    resolve: {
      descriptiveDocumentText: DescriptiveDocumentTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DescriptiveDocumentTextFaUpdateComponent,
    resolve: {
      descriptiveDocumentText: DescriptiveDocumentTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DescriptiveDocumentTextFaUpdateComponent,
    resolve: {
      descriptiveDocumentText: DescriptiveDocumentTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(descriptiveDocumentTextRoute)],
  exports: [RouterModule],
})
export class DescriptiveDocumentTextFaRoutingModule {}
