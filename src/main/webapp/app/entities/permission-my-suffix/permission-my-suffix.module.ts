import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermissionMySuffixComponent } from './list/permission-my-suffix.component';
import { PermissionMySuffixDetailComponent } from './detail/permission-my-suffix-detail.component';
import { PermissionMySuffixUpdateComponent } from './update/permission-my-suffix-update.component';
import { PermissionMySuffixDeleteDialogComponent } from './delete/permission-my-suffix-delete-dialog.component';
import { PermissionMySuffixRoutingModule } from './route/permission-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, PermissionMySuffixRoutingModule],
  declarations: [
    PermissionMySuffixComponent,
    PermissionMySuffixDetailComponent,
    PermissionMySuffixUpdateComponent,
    PermissionMySuffixDeleteDialogComponent,
  ],
  entryComponents: [PermissionMySuffixDeleteDialogComponent],
})
export class PermissionMySuffixModule {}
