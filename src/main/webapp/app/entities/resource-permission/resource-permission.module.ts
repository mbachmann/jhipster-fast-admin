import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResourcePermissionComponent } from './list/resource-permission.component';
import { ResourcePermissionDetailComponent } from './detail/resource-permission-detail.component';
import { ResourcePermissionUpdateComponent } from './update/resource-permission-update.component';
import { ResourcePermissionDeleteDialogComponent } from './delete/resource-permission-delete-dialog.component';
import { ResourcePermissionRoutingModule } from './route/resource-permission-routing.module';

@NgModule({
  imports: [SharedModule, ResourcePermissionRoutingModule],
  declarations: [
    ResourcePermissionComponent,
    ResourcePermissionDetailComponent,
    ResourcePermissionUpdateComponent,
    ResourcePermissionDeleteDialogComponent,
  ],
  entryComponents: [ResourcePermissionDeleteDialogComponent],
})
export class ResourcePermissionModule {}
