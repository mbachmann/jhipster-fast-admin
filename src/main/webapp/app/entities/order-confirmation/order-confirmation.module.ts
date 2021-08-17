import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderConfirmationComponent } from './list/order-confirmation.component';
import { OrderConfirmationDetailComponent } from './detail/order-confirmation-detail.component';
import { OrderConfirmationUpdateComponent } from './update/order-confirmation-update.component';
import { OrderConfirmationDeleteDialogComponent } from './delete/order-confirmation-delete-dialog.component';
import { OrderConfirmationRoutingModule } from './route/order-confirmation-routing.module';

@NgModule({
  imports: [SharedModule, OrderConfirmationRoutingModule],
  declarations: [
    OrderConfirmationComponent,
    OrderConfirmationDetailComponent,
    OrderConfirmationUpdateComponent,
    OrderConfirmationDeleteDialogComponent,
  ],
  entryComponents: [OrderConfirmationDeleteDialogComponent],
})
export class OrderConfirmationModule {}
