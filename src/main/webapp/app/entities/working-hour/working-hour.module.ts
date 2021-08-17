import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkingHourComponent } from './list/working-hour.component';
import { WorkingHourDetailComponent } from './detail/working-hour-detail.component';
import { WorkingHourUpdateComponent } from './update/working-hour-update.component';
import { WorkingHourDeleteDialogComponent } from './delete/working-hour-delete-dialog.component';
import { WorkingHourRoutingModule } from './route/working-hour-routing.module';

@NgModule({
  imports: [SharedModule, WorkingHourRoutingModule],
  declarations: [WorkingHourComponent, WorkingHourDetailComponent, WorkingHourUpdateComponent, WorkingHourDeleteDialogComponent],
  entryComponents: [WorkingHourDeleteDialogComponent],
})
export class WorkingHourModule {}
