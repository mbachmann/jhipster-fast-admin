import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentLetterComponent } from '../list/document-letter.component';
import { DocumentLetterDetailComponent } from '../detail/document-letter-detail.component';
import { DocumentLetterUpdateComponent } from '../update/document-letter-update.component';
import { DocumentLetterRoutingResolveService } from './document-letter-routing-resolve.service';

const documentLetterRoute: Routes = [
  {
    path: '',
    component: DocumentLetterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentLetterDetailComponent,
    resolve: {
      documentLetter: DocumentLetterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentLetterUpdateComponent,
    resolve: {
      documentLetter: DocumentLetterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentLetterUpdateComponent,
    resolve: {
      documentLetter: DocumentLetterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentLetterRoute)],
  exports: [RouterModule],
})
export class DocumentLetterRoutingModule {}
