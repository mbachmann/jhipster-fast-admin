import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EffortFaComponent } from './list/effort-fa.component';
import { EffortFaDetailComponent } from './detail/effort-fa-detail.component';
import { EffortFaUpdateComponent } from './update/effort-fa-update.component';
import { EffortFaDeleteDialogComponent } from './delete/effort-fa-delete-dialog.component';
import { EffortFaRoutingModule } from './route/effort-fa-routing.module';

@NgModule({
  imports: [SharedModule, EffortFaRoutingModule],
  declarations: [EffortFaComponent, EffortFaDetailComponent, EffortFaUpdateComponent, EffortFaDeleteDialogComponent],
  entryComponents: [EffortFaDeleteDialogComponent],
})
export class EffortFaModule {}
