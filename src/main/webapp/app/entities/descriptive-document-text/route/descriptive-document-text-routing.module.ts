import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DescriptiveDocumentTextComponent } from '../list/descriptive-document-text.component';
import { DescriptiveDocumentTextDetailComponent } from '../detail/descriptive-document-text-detail.component';
import { DescriptiveDocumentTextUpdateComponent } from '../update/descriptive-document-text-update.component';
import { DescriptiveDocumentTextRoutingResolveService } from './descriptive-document-text-routing-resolve.service';

const descriptiveDocumentTextRoute: Routes = [
  {
    path: '',
    component: DescriptiveDocumentTextComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DescriptiveDocumentTextDetailComponent,
    resolve: {
      descriptiveDocumentText: DescriptiveDocumentTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DescriptiveDocumentTextUpdateComponent,
    resolve: {
      descriptiveDocumentText: DescriptiveDocumentTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DescriptiveDocumentTextUpdateComponent,
    resolve: {
      descriptiveDocumentText: DescriptiveDocumentTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(descriptiveDocumentTextRoute)],
  exports: [RouterModule],
})
export class DescriptiveDocumentTextRoutingModule {}
