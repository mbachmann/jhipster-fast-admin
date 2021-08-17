import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderConfirmationFaComponent } from './list/order-confirmation-fa.component';
import { OrderConfirmationFaDetailComponent } from './detail/order-confirmation-fa-detail.component';
import { OrderConfirmationFaUpdateComponent } from './update/order-confirmation-fa-update.component';
import { OrderConfirmationFaDeleteDialogComponent } from './delete/order-confirmation-fa-delete-dialog.component';
import { OrderConfirmationFaRoutingModule } from './route/order-confirmation-fa-routing.module';

@NgModule({
  imports: [SharedModule, OrderConfirmationFaRoutingModule],
  declarations: [
    OrderConfirmationFaComponent,
    OrderConfirmationFaDetailComponent,
    OrderConfirmationFaUpdateComponent,
    OrderConfirmationFaDeleteDialogComponent,
  ],
  entryComponents: [OrderConfirmationFaDeleteDialogComponent],
})
export class OrderConfirmationFaModule {}
