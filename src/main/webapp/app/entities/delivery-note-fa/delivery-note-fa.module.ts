import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryNoteFaComponent } from './list/delivery-note-fa.component';
import { DeliveryNoteFaDetailComponent } from './detail/delivery-note-fa-detail.component';
import { DeliveryNoteFaUpdateComponent } from './update/delivery-note-fa-update.component';
import { DeliveryNoteFaDeleteDialogComponent } from './delete/delivery-note-fa-delete-dialog.component';
import { DeliveryNoteFaRoutingModule } from './route/delivery-note-fa-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryNoteFaRoutingModule],
  declarations: [
    DeliveryNoteFaComponent,
    DeliveryNoteFaDetailComponent,
    DeliveryNoteFaUpdateComponent,
    DeliveryNoteFaDeleteDialogComponent,
  ],
  entryComponents: [DeliveryNoteFaDeleteDialogComponent],
})
export class DeliveryNoteFaModule {}
