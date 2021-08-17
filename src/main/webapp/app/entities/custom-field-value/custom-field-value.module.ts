import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomFieldValueComponent } from './list/custom-field-value.component';
import { CustomFieldValueDetailComponent } from './detail/custom-field-value-detail.component';
import { CustomFieldValueUpdateComponent } from './update/custom-field-value-update.component';
import { CustomFieldValueDeleteDialogComponent } from './delete/custom-field-value-delete-dialog.component';
import { CustomFieldValueRoutingModule } from './route/custom-field-value-routing.module';

@NgModule({
  imports: [SharedModule, CustomFieldValueRoutingModule],
  declarations: [
    CustomFieldValueComponent,
    CustomFieldValueDetailComponent,
    CustomFieldValueUpdateComponent,
    CustomFieldValueDeleteDialogComponent,
  ],
  entryComponents: [CustomFieldValueDeleteDialogComponent],
})
export class CustomFieldValueModule {}
