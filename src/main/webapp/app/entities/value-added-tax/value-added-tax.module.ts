import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ValueAddedTaxComponent } from './list/value-added-tax.component';
import { ValueAddedTaxDetailComponent } from './detail/value-added-tax-detail.component';
import { ValueAddedTaxUpdateComponent } from './update/value-added-tax-update.component';
import { ValueAddedTaxDeleteDialogComponent } from './delete/value-added-tax-delete-dialog.component';
import { ValueAddedTaxRoutingModule } from './route/value-added-tax-routing.module';

@NgModule({
  imports: [SharedModule, ValueAddedTaxRoutingModule],
  declarations: [ValueAddedTaxComponent, ValueAddedTaxDetailComponent, ValueAddedTaxUpdateComponent, ValueAddedTaxDeleteDialogComponent],
  entryComponents: [ValueAddedTaxDeleteDialogComponent],
})
export class ValueAddedTaxModule {}
