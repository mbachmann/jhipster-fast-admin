import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VatFaComponent } from './list/vat-fa.component';
import { VatFaDetailComponent } from './detail/vat-fa-detail.component';
import { VatFaUpdateComponent } from './update/vat-fa-update.component';
import { VatFaDeleteDialogComponent } from './delete/vat-fa-delete-dialog.component';
import { VatFaRoutingModule } from './route/vat-fa-routing.module';

@NgModule({
  imports: [SharedModule, VatFaRoutingModule],
  declarations: [VatFaComponent, VatFaDetailComponent, VatFaUpdateComponent, VatFaDeleteDialogComponent],
  entryComponents: [VatFaDeleteDialogComponent],
})
export class VatFaModule {}
