import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomFieldMySuffixComponent } from './list/custom-field-my-suffix.component';
import { CustomFieldMySuffixDetailComponent } from './detail/custom-field-my-suffix-detail.component';
import { CustomFieldMySuffixUpdateComponent } from './update/custom-field-my-suffix-update.component';
import { CustomFieldMySuffixDeleteDialogComponent } from './delete/custom-field-my-suffix-delete-dialog.component';
import { CustomFieldMySuffixRoutingModule } from './route/custom-field-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, CustomFieldMySuffixRoutingModule],
  declarations: [
    CustomFieldMySuffixComponent,
    CustomFieldMySuffixDetailComponent,
    CustomFieldMySuffixUpdateComponent,
    CustomFieldMySuffixDeleteDialogComponent,
  ],
  entryComponents: [CustomFieldMySuffixDeleteDialogComponent],
})
export class CustomFieldMySuffixModule {}
