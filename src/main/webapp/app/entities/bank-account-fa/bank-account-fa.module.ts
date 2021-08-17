import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BankAccountFaComponent } from './list/bank-account-fa.component';
import { BankAccountFaDetailComponent } from './detail/bank-account-fa-detail.component';
import { BankAccountFaUpdateComponent } from './update/bank-account-fa-update.component';
import { BankAccountFaDeleteDialogComponent } from './delete/bank-account-fa-delete-dialog.component';
import { BankAccountFaRoutingModule } from './route/bank-account-fa-routing.module';

@NgModule({
  imports: [SharedModule, BankAccountFaRoutingModule],
  declarations: [BankAccountFaComponent, BankAccountFaDetailComponent, BankAccountFaUpdateComponent, BankAccountFaDeleteDialogComponent],
  entryComponents: [BankAccountFaDeleteDialogComponent],
})
export class BankAccountFaModule {}
