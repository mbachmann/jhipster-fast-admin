import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OfferFaComponent } from './list/offer-fa.component';
import { OfferFaDetailComponent } from './detail/offer-fa-detail.component';
import { OfferFaUpdateComponent } from './update/offer-fa-update.component';
import { OfferFaDeleteDialogComponent } from './delete/offer-fa-delete-dialog.component';
import { OfferFaRoutingModule } from './route/offer-fa-routing.module';

@NgModule({
  imports: [SharedModule, OfferFaRoutingModule],
  declarations: [OfferFaComponent, OfferFaDetailComponent, OfferFaUpdateComponent, OfferFaDeleteDialogComponent],
  entryComponents: [OfferFaDeleteDialogComponent],
})
export class OfferFaModule {}
