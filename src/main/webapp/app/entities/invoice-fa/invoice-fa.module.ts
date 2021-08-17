import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvoiceFaComponent } from './list/invoice-fa.component';
import { InvoiceFaDetailComponent } from './detail/invoice-fa-detail.component';
import { InvoiceFaUpdateComponent } from './update/invoice-fa-update.component';
import { InvoiceFaDeleteDialogComponent } from './delete/invoice-fa-delete-dialog.component';
import { InvoiceFaRoutingModule } from './route/invoice-fa-routing.module';

@NgModule({
  imports: [SharedModule, InvoiceFaRoutingModule],
  declarations: [InvoiceFaComponent, InvoiceFaDetailComponent, InvoiceFaUpdateComponent, InvoiceFaDeleteDialogComponent],
  entryComponents: [InvoiceFaDeleteDialogComponent],
})
export class InvoiceFaModule {}
