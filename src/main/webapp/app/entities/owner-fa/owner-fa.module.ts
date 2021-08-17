import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OwnerFaComponent } from './list/owner-fa.component';
import { OwnerFaDetailComponent } from './detail/owner-fa-detail.component';
import { OwnerFaUpdateComponent } from './update/owner-fa-update.component';
import { OwnerFaDeleteDialogComponent } from './delete/owner-fa-delete-dialog.component';
import { OwnerFaRoutingModule } from './route/owner-fa-routing.module';

@NgModule({
  imports: [SharedModule, OwnerFaRoutingModule],
  declarations: [OwnerFaComponent, OwnerFaDetailComponent, OwnerFaUpdateComponent, OwnerFaDeleteDialogComponent],
  entryComponents: [OwnerFaDeleteDialogComponent],
})
export class OwnerFaModule {}
