import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActivityFaComponent } from './list/activity-fa.component';
import { ActivityFaDetailComponent } from './detail/activity-fa-detail.component';
import { ActivityFaUpdateComponent } from './update/activity-fa-update.component';
import { ActivityFaDeleteDialogComponent } from './delete/activity-fa-delete-dialog.component';
import { ActivityFaRoutingModule } from './route/activity-fa-routing.module';

@NgModule({
  imports: [SharedModule, ActivityFaRoutingModule],
  declarations: [ActivityFaComponent, ActivityFaDetailComponent, ActivityFaUpdateComponent, ActivityFaDeleteDialogComponent],
  entryComponents: [ActivityFaDeleteDialogComponent],
})
export class ActivityFaModule {}
