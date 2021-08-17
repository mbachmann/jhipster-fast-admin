import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceFaComponent } from '../list/invoice-fa.component';
import { InvoiceFaDetailComponent } from '../detail/invoice-fa-detail.component';
import { InvoiceFaUpdateComponent } from '../update/invoice-fa-update.component';
import { InvoiceFaRoutingResolveService } from './invoice-fa-routing-resolve.service';

const invoiceRoute: Routes = [
  {
    path: '',
    component: InvoiceFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceFaDetailComponent,
    resolve: {
      invoice: InvoiceFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceFaUpdateComponent,
    resolve: {
      invoice: InvoiceFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceFaUpdateComponent,
    resolve: {
      invoice: InvoiceFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invoiceRoute)],
  exports: [RouterModule],
})
export class InvoiceFaRoutingModule {}
