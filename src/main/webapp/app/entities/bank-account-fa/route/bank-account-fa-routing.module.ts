import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankAccountFaComponent } from '../list/bank-account-fa.component';
import { BankAccountFaDetailComponent } from '../detail/bank-account-fa-detail.component';
import { BankAccountFaUpdateComponent } from '../update/bank-account-fa-update.component';
import { BankAccountFaRoutingResolveService } from './bank-account-fa-routing-resolve.service';

const bankAccountRoute: Routes = [
  {
    path: '',
    component: BankAccountFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankAccountFaDetailComponent,
    resolve: {
      bankAccount: BankAccountFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankAccountFaUpdateComponent,
    resolve: {
      bankAccount: BankAccountFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankAccountFaUpdateComponent,
    resolve: {
      bankAccount: BankAccountFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bankAccountRoute)],
  exports: [RouterModule],
})
export class BankAccountFaRoutingModule {}
