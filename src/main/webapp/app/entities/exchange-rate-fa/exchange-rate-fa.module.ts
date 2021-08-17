import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExchangeRateFaComponent } from './list/exchange-rate-fa.component';
import { ExchangeRateFaDetailComponent } from './detail/exchange-rate-fa-detail.component';
import { ExchangeRateFaUpdateComponent } from './update/exchange-rate-fa-update.component';
import { ExchangeRateFaDeleteDialogComponent } from './delete/exchange-rate-fa-delete-dialog.component';
import { ExchangeRateFaRoutingModule } from './route/exchange-rate-fa-routing.module';

@NgModule({
  imports: [SharedModule, ExchangeRateFaRoutingModule],
  declarations: [
    ExchangeRateFaComponent,
    ExchangeRateFaDetailComponent,
    ExchangeRateFaUpdateComponent,
    ExchangeRateFaDeleteDialogComponent,
  ],
  entryComponents: [ExchangeRateFaDeleteDialogComponent],
})
export class ExchangeRateFaModule {}
