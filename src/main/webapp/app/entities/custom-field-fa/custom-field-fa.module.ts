import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomFieldFaComponent } from './list/custom-field-fa.component';
import { CustomFieldFaDetailComponent } from './detail/custom-field-fa-detail.component';
import { CustomFieldFaUpdateComponent } from './update/custom-field-fa-update.component';
import { CustomFieldFaDeleteDialogComponent } from './delete/custom-field-fa-delete-dialog.component';
import { CustomFieldFaRoutingModule } from './route/custom-field-fa-routing.module';

@NgModule({
  imports: [SharedModule, CustomFieldFaRoutingModule],
  declarations: [CustomFieldFaComponent, CustomFieldFaDetailComponent, CustomFieldFaUpdateComponent, CustomFieldFaDeleteDialogComponent],
  entryComponents: [CustomFieldFaDeleteDialogComponent],
})
export class CustomFieldFaModule {}
