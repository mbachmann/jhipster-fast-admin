import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoleMySuffixComponent } from './list/role-my-suffix.component';
import { RoleMySuffixDetailComponent } from './detail/role-my-suffix-detail.component';
import { RoleMySuffixUpdateComponent } from './update/role-my-suffix-update.component';
import { RoleMySuffixDeleteDialogComponent } from './delete/role-my-suffix-delete-dialog.component';
import { RoleMySuffixRoutingModule } from './route/role-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, RoleMySuffixRoutingModule],
  declarations: [RoleMySuffixComponent, RoleMySuffixDetailComponent, RoleMySuffixUpdateComponent, RoleMySuffixDeleteDialogComponent],
  entryComponents: [RoleMySuffixDeleteDialogComponent],
})
export class RoleMySuffixModule {}
