import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EffortComponent } from './list/effort.component';
import { EffortDetailComponent } from './detail/effort-detail.component';
import { EffortUpdateComponent } from './update/effort-update.component';
import { EffortDeleteDialogComponent } from './delete/effort-delete-dialog.component';
import { EffortRoutingModule } from './route/effort-routing.module';

@NgModule({
  imports: [SharedModule, EffortRoutingModule],
  declarations: [EffortComponent, EffortDetailComponent, EffortUpdateComponent, EffortDeleteDialogComponent],
  entryComponents: [EffortDeleteDialogComponent],
})
export class EffortModule {}
