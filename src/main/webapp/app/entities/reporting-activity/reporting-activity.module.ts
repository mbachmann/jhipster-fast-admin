import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportingActivityComponent } from './list/reporting-activity.component';
import { ReportingActivityDetailComponent } from './detail/reporting-activity-detail.component';
import { ReportingActivityUpdateComponent } from './update/reporting-activity-update.component';
import { ReportingActivityDeleteDialogComponent } from './delete/reporting-activity-delete-dialog.component';
import { ReportingActivityRoutingModule } from './route/reporting-activity-routing.module';

@NgModule({
  imports: [SharedModule, ReportingActivityRoutingModule],
  declarations: [
    ReportingActivityComponent,
    ReportingActivityDetailComponent,
    ReportingActivityUpdateComponent,
    ReportingActivityDeleteDialogComponent,
  ],
  entryComponents: [ReportingActivityDeleteDialogComponent],
})
export class ReportingActivityModule {}
