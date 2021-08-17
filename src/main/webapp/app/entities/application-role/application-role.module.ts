import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApplicationRoleComponent } from './list/application-role.component';
import { ApplicationRoleDetailComponent } from './detail/application-role-detail.component';
import { ApplicationRoleUpdateComponent } from './update/application-role-update.component';
import { ApplicationRoleDeleteDialogComponent } from './delete/application-role-delete-dialog.component';
import { ApplicationRoleRoutingModule } from './route/application-role-routing.module';

@NgModule({
  imports: [SharedModule, ApplicationRoleRoutingModule],
  declarations: [
    ApplicationRoleComponent,
    ApplicationRoleDetailComponent,
    ApplicationRoleUpdateComponent,
    ApplicationRoleDeleteDialogComponent,
  ],
  entryComponents: [ApplicationRoleDeleteDialogComponent],
})
export class ApplicationRoleModule {}
