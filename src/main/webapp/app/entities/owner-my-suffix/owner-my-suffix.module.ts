import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OwnerMySuffixComponent } from './list/owner-my-suffix.component';
import { OwnerMySuffixDetailComponent } from './detail/owner-my-suffix-detail.component';
import { OwnerMySuffixUpdateComponent } from './update/owner-my-suffix-update.component';
import { OwnerMySuffixDeleteDialogComponent } from './delete/owner-my-suffix-delete-dialog.component';
import { OwnerMySuffixRoutingModule } from './route/owner-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, OwnerMySuffixRoutingModule],
  declarations: [OwnerMySuffixComponent, OwnerMySuffixDetailComponent, OwnerMySuffixUpdateComponent, OwnerMySuffixDeleteDialogComponent],
  entryComponents: [OwnerMySuffixDeleteDialogComponent],
})
export class OwnerMySuffixModule {}
