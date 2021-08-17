import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentLetterFaComponent } from '../list/document-letter-fa.component';
import { DocumentLetterFaDetailComponent } from '../detail/document-letter-fa-detail.component';
import { DocumentLetterFaUpdateComponent } from '../update/document-letter-fa-update.component';
import { DocumentLetterFaRoutingResolveService } from './document-letter-fa-routing-resolve.service';

const documentLetterRoute: Routes = [
  {
    path: '',
    component: DocumentLetterFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentLetterFaDetailComponent,
    resolve: {
      documentLetter: DocumentLetterFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentLetterFaUpdateComponent,
    resolve: {
      documentLetter: DocumentLetterFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentLetterFaUpdateComponent,
    resolve: {
      documentLetter: DocumentLetterFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentLetterRoute)],
  exports: [RouterModule],
})
export class DocumentLetterFaRoutingModule {}
