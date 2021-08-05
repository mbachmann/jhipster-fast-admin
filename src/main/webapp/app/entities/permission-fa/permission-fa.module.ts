import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermissionFaComponent } from './list/permission-fa.component';
import { PermissionFaDetailComponent } from './detail/permission-fa-detail.component';
import { PermissionFaUpdateComponent } from './update/permission-fa-update.component';
import { PermissionFaDeleteDialogComponent } from './delete/permission-fa-delete-dialog.component';
import { PermissionFaRoutingModule } from './route/permission-fa-routing.module';

@NgModule({
  imports: [SharedModule, PermissionFaRoutingModule],
  declarations: [PermissionFaComponent, PermissionFaDetailComponent, PermissionFaUpdateComponent, PermissionFaDeleteDialogComponent],
  entryComponents: [PermissionFaDeleteDialogComponent],
})
export class PermissionFaModule {}
