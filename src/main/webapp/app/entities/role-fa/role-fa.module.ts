import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoleFaComponent } from './list/role-fa.component';
import { RoleFaDetailComponent } from './detail/role-fa-detail.component';
import { RoleFaUpdateComponent } from './update/role-fa-update.component';
import { RoleFaDeleteDialogComponent } from './delete/role-fa-delete-dialog.component';
import { RoleFaRoutingModule } from './route/role-fa-routing.module';

@NgModule({
  imports: [SharedModule, RoleFaRoutingModule],
  declarations: [RoleFaComponent, RoleFaDetailComponent, RoleFaUpdateComponent, RoleFaDeleteDialogComponent],
  entryComponents: [RoleFaDeleteDialogComponent],
})
export class RoleFaModule {}
