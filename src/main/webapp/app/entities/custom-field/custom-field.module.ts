import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomFieldComponent } from './list/custom-field.component';
import { CustomFieldDetailComponent } from './detail/custom-field-detail.component';
import { CustomFieldUpdateComponent } from './update/custom-field-update.component';
import { CustomFieldDeleteDialogComponent } from './delete/custom-field-delete-dialog.component';
import { CustomFieldRoutingModule } from './route/custom-field-routing.module';

@NgModule({
  imports: [SharedModule, CustomFieldRoutingModule],
  declarations: [CustomFieldComponent, CustomFieldDetailComponent, CustomFieldUpdateComponent, CustomFieldDeleteDialogComponent],
  entryComponents: [CustomFieldDeleteDialogComponent],
})
export class CustomFieldModule {}
